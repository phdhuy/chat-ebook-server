package com.chatebook.chat.service;

import com.chatebook.chat.payload.request.CreateMessageRequest;
import com.chatebook.chat.payload.response.MessageInfoResponse;

import java.util.UUID;

public interface MessageService {

  MessageInfoResponse sendMessage(UUID userId, UUID conversationId, CreateMessageRequest createMessageRequest);
}
