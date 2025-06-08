package com.chatebook.chat.service;

import com.chatebook.chat.model.Conversation;
import com.chatebook.chat.payload.request.CreateConversationByURLRequest;
import com.chatebook.chat.payload.request.UpdateConversationRequest;
import com.chatebook.chat.payload.response.ConversationInfoResponse;
import com.chatebook.common.payload.general.ResponseDataAPI;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ConversationService {

  ConversationInfoResponse createConversation(UUID userId, MultipartFile file);

  ConversationInfoResponse createConversationByURL(
      UUID userId, CreateConversationByURLRequest createConversationByURLRequest);

  ResponseDataAPI getMyConversations(Pageable pageable, UUID userId);

  ConversationInfoResponse getDetailConversation(UUID userId, UUID conversationId);

  Conversation findById(UUID conversationId);

  Conversation checkConversationOwnership(UUID conversationId, UUID userId);

  void deleteConversation(UUID conversationId, UUID userId);

  ConversationInfoResponse updateInfoConversation(
      UUID userId, UUID conversationId, UpdateConversationRequest updateConversationRequest);

  ResponseDataAPI getListConversationByAdmin(Pageable pageable, String query);
}
