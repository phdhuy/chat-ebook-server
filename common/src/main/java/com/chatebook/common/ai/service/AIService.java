package com.chatebook.common.ai.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.multipart.MultipartFile;

public interface AIService {

  void uploadFile(MultipartFile file, UUID conversationId);

  CompletableFuture<String> sendMessage(String content, String historyConversation, UUID userId);

  CompletableFuture<String> summarizeEbook(UUID conversationId, UUID userId);
}
