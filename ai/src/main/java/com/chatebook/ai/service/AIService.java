package com.chatebook.ai.service;

import com.chatebook.ai.payload.response.GenerateMindMapResponse;
import com.chatebook.ai.payload.response.QueryAIResponse;
import com.chatebook.ai.payload.response.UploadFileAIResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.multipart.MultipartFile;

public interface AIService {

  UploadFileAIResponse uploadFile(MultipartFile file, UUID conversationId, UUID userId);

  CompletableFuture<QueryAIResponse> sendMessage(
      String content, String historyConversation, UUID userId, UUID conversationId);

  CompletableFuture<String> summarizeEbook(UUID conversationId, UUID userId);

  GenerateMindMapResponse generateMindMap(MultipartFile file, UUID conversationId, UUID userId);
}
