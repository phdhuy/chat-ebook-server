package com.chatebook.chat.service;

import com.chatebook.chat.model.Conversation;
import com.chatebook.chat.payload.response.ConversationInfoResponse;
import com.chatebook.common.payload.general.ResponseDataAPI;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ConversationService {

  ConversationInfoResponse createConversation(UUID userId, MultipartFile file);

  ResponseDataAPI getMyConversations(Pageable pageable, UUID userId);

  ConversationInfoResponse getDetailConversation(UUID userId, UUID conversationId);

  Conversation findById(UUID conversationId);
}
