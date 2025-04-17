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
import com.chatebook.common.config.RabbitMQConfig;
import com.chatebook.common.constant.MessageConstant;
import com.chatebook.common.exception.ForbiddenException;
import com.chatebook.common.utils.RabbitMQAdapter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;

  private final ConversationService conversationService;

  private final RabbitMQAdapter rabbitMQAdapter;

  private final MessageMapper messageMapper;

  @Override
  @Transactional
  public MessageInfoResponse sendMessage(
      UUID userId, UUID conversationId, CreateMessageRequest createMessageRequest) {
    Conversation conversation = conversationService.findById(conversationId);

    if (!conversation.getUser().getId().equals(userId)) {
      throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
    }

    Message newMessage = new Message();

    newMessage.setContent(createMessageRequest.getContent());
    newMessage.setSenderType(SenderType.USER);
    newMessage.setConversation(conversation);

    MessageInfoResponse messageInfoResponse =
        messageMapper.toMessageInfoResponse(messageRepository.save(newMessage), conversationId);

    rabbitMQAdapter.pushMessageToQueue(
        RabbitMQConfig.DIRECT_EXCHANGE_NAME, userId.toString(), messageInfoResponse);

    return messageInfoResponse;
  }
}
