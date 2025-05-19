package com.chatebook.chat.service;

import com.chatebook.chat.payload.request.CreateMessageRequest;
import com.chatebook.chat.payload.response.MessageInfoResponse;
import com.chatebook.common.payload.general.ResponseDataAPI;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageInfoResponse sendMessage(
      UUID userId, UUID conversationId, CreateMessageRequest createMessageRequest);

  ResponseDataAPI getListMessageByConversationId(
      Pageable pageable, UUID userId, UUID conversationId);

  MessageInfoResponse summarizeEbook(UUID userId, UUID conversationId, CreateMessageRequest createMessageRequest);
}
