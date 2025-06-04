package com.chatebook.chat.service.impl;

import com.chatebook.ai.payload.response.QueryAIResponse;
import com.chatebook.ai.service.AIService;
import com.chatebook.chat.event.ConversationCreatedEvent;
import com.chatebook.chat.mapper.MessageMapper;
import com.chatebook.chat.model.CitedExcerpt;
import com.chatebook.chat.model.Conversation;
import com.chatebook.chat.model.Message;
import com.chatebook.chat.model.enums.SenderType;
import com.chatebook.chat.payload.request.CreateMessageRequest;
import com.chatebook.chat.payload.response.MessageInfoResponse;
import com.chatebook.chat.repository.MessageRepository;
import com.chatebook.chat.service.ConversationService;
import com.chatebook.chat.service.MessageService;
import com.chatebook.common.config.RabbitMQConfig;
import com.chatebook.common.payload.general.PageInfo;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.common.utils.PagingUtils;
import com.chatebook.common.utils.RabbitMQAdapter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;

  private final ConversationService conversationService;

  private final AIService aiService;

  private final RabbitMQAdapter rabbitMQAdapter;

  private final MessageMapper messageMapper;

  @Override
  public MessageInfoResponse sendMessage(
      UUID userId, UUID conversationId, CreateMessageRequest request) {
    Conversation conversation =
        conversationService.checkConversationOwnership(conversationId, userId);

    MessageInfoResponse response =
        saveAndPublishMessage(
            conversation, request.getContent(), SenderType.USER, userId.toString());

    CompletableFuture<QueryAIResponse> aiResponseFuture =
        aiService.sendMessage(
            request.getContent(),
            this.getListMessageRecentlyAndFormatToString(userId, conversationId),
            userId,
            conversationId);
    processQueryAIResponse(aiResponseFuture, conversation, userId.toString());

    return response;
  }

  @Override
  public ResponseDataAPI getListMessageByConversationId(
      Pageable pageable, UUID userId, UUID conversationId) {
    Conversation conversation =
        conversationService.checkConversationOwnership(conversationId, userId);

    Page<Message> page = messageRepository.findAllByConversationId(pageable, conversation.getId());

    List<MessageInfoResponse> data =
        new java.util.ArrayList<>(
            page.stream()
                .map(msg -> messageMapper.toMessageInfoResponse(msg, conversationId))
                .toList());

    Collections.reverse(data);

    PageInfo pageInfo =
        new PageInfo(pageable.getPageNumber() + 1, page.getTotalPages(), page.getTotalElements());

    return ResponseDataAPI.success(data, pageInfo);
  }

  @Override
  public MessageInfoResponse summarizeEbook(
      UUID userId, UUID conversationId, CreateMessageRequest request) {
    Conversation conversation =
        conversationService.checkConversationOwnership(conversationId, userId);

    MessageInfoResponse response =
        saveAndPublishMessage(
            conversation, request.getContent(), SenderType.USER, userId.toString());

    CompletableFuture<String> aiResponseFuture = aiService.summarizeEbook(conversationId, userId);
    processAIResponse(aiResponseFuture, conversation, userId.toString());

    return response;
  }

  @EventListener
  public void handleConversationCreated(ConversationCreatedEvent event) {
    saveAndPublishMessage(
        event.getConversation(),
        event.getUploadResponse().getAnswer(),
        SenderType.BOT,
        event.getUserId().toString());
  }

  private void processAIResponse(
      CompletableFuture<String> aiResponseFuture,
      Conversation conversation,
      String queueRoutingKey) {
    aiResponseFuture
        .thenAccept(
            aiResponse ->
                saveAndPublishMessage(conversation, aiResponse, SenderType.BOT, queueRoutingKey))
        .exceptionally(
            throwable -> {
              log.error("Failed to process AI response: {}", throwable.getMessage());
              return null;
            });
  }

  private void processQueryAIResponse(
      CompletableFuture<QueryAIResponse> aiResponseFuture,
      Conversation conversation,
      String queueRoutingKey) {
    aiResponseFuture
        .thenAccept(
            aiResponse ->
                saveAndPublishMessageCitedExcerpt(conversation, aiResponse, queueRoutingKey))
        .exceptionally(
            throwable -> {
              log.error("Failed to process AI response: {}", throwable.getMessage());
              return null;
            });
  }

  private void saveAndPublishMessageCitedExcerpt(
      Conversation conversation, QueryAIResponse content, String queueRoutingKey) {
    Message message = new Message();

    message.setContent(content.getAnswer());
    message.setSenderType(SenderType.BOT);
    message.setConversation(conversation);

    List<CitedExcerpt> excerpts =
        content.getCitedExcerpts().stream()
            .map(
                e ->
                    CitedExcerpt.builder()
                        .sourceId(e.getId())
                        .page(e.getPage())
                        .score(e.getScore())
                        .text(e.getText())
                        .message(message)
                        .build())
            .toList();

    message.setCitedExcerpts(excerpts);

    MessageInfoResponse response =
        messageMapper.toMessageInfoResponse(messageRepository.save(message), conversation.getId());

    rabbitMQAdapter.pushMessageToQueue(
        RabbitMQConfig.DIRECT_EXCHANGE_NAME, queueRoutingKey, response);
  }

  private MessageInfoResponse saveAndPublishMessage(
      Conversation conversation, String content, SenderType senderType, String queueRoutingKey) {
    Message message = new Message();

    message.setContent(content);
    message.setSenderType(senderType);
    message.setConversation(conversation);
    message.setCitedExcerpts(List.of());

    MessageInfoResponse response =
        messageMapper.toMessageInfoResponse(messageRepository.save(message), conversation.getId());

    rabbitMQAdapter.pushMessageToQueue(
        RabbitMQConfig.DIRECT_EXCHANGE_NAME, queueRoutingKey, response);

    return response;
  }

  private String getListMessageRecentlyAndFormatToString(UUID userId, UUID conversationId) {
    Conversation conversation =
        conversationService.checkConversationOwnership(conversationId, userId);

    Page<Message> page =
        messageRepository.findAllByConversationId(
            PagingUtils.makePageRequestWithCamelCase("id", "desc", 1, 3), conversation.getId());

    List<MessageInfoResponse> data =
        new java.util.ArrayList<>(
            page.stream()
                .map(msg -> messageMapper.toMessageInfoResponse(msg, conversationId))
                .toList());

    Collections.reverse(data);

    return data.stream()
        .map(
            msg ->
                (Objects.equals(msg.getSenderType(), SenderType.USER.toString())
                        ? "User: "
                        : "Assistant: ")
                    + msg.getContent())
        .collect(Collectors.joining("\n"));
  }
}
