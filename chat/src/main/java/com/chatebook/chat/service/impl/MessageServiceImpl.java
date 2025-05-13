package com.chatebook.chat.service.impl;

import com.chatebook.chat.mapper.MessageMapper;
import com.chatebook.chat.model.Conversation;
import com.chatebook.chat.model.Message;
import com.chatebook.chat.model.enums.SenderType;
import com.chatebook.chat.payload.request.CreateMessageRequest;
import com.chatebook.chat.payload.response.MessageInfoResponse;
import com.chatebook.chat.repository.MessageRepository;
import com.chatebook.chat.service.ConversationService;
import com.chatebook.chat.service.MessageService;
import com.chatebook.common.ai.service.AIService;
import com.chatebook.common.config.RabbitMQConfig;
import com.chatebook.common.payload.general.PageInfo;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.common.utils.RabbitMQAdapter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;

  private final ConversationService conversationService;

  private final AIService aiService;

  private final RabbitMQAdapter rabbitMQAdapter;

  private final MessageMapper messageMapper;

  @Override
  @Transactional
  public MessageInfoResponse sendMessage(
      UUID userId, UUID conversationId, CreateMessageRequest createMessageRequest) {
    Conversation conversation =
        conversationService.checkConversationOwnership(conversationId, userId);

    Message newMessage = new Message();

    newMessage.setContent(createMessageRequest.getContent());
    newMessage.setSenderType(SenderType.USER);
    newMessage.setConversation(conversation);

    MessageInfoResponse messageInfoResponse =
        messageMapper.toMessageInfoResponse(messageRepository.save(newMessage), conversationId);

    rabbitMQAdapter.pushMessageToQueue(
        RabbitMQConfig.DIRECT_EXCHANGE_NAME, userId.toString(), messageInfoResponse);

    CompletableFuture<String> aiResponseFuture =
        aiService.sendMessage(createMessageRequest.getContent(), userId);
    aiResponseFuture
        .thenAccept(
            aiResponse -> {
              Message aiMessage = new Message();

              aiMessage.setContent(aiResponse);
              aiMessage.setSenderType(SenderType.BOT);
              aiMessage.setConversation(conversation);

              MessageInfoResponse aiMessageInfoResponse =
                  messageMapper.toMessageInfoResponse(
                      messageRepository.save(aiMessage), conversationId);

              rabbitMQAdapter.pushMessageToQueue(
                  RabbitMQConfig.DIRECT_EXCHANGE_NAME, userId.toString(), aiMessageInfoResponse);
            })
        .exceptionally(
            throwable -> {
              log.error("Failed to process AI response: {}", throwable.getMessage());
              return null;
            });

    return messageInfoResponse;
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
}
