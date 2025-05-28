package com.chatebook.ai.service;

import com.chatebook.ai.payload.response.QueryAIResponse;
import com.chatebook.ai.payload.response.UploadFileAIResponse;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.multipart.MultipartFile;

public interface AIService {

  UploadFileAIResponse uploadFile(MultipartFile file, UUID conversationId) throws IOException;

  CompletableFuture<QueryAIResponse> sendMessage(String content, String historyConversation, UUID userId);

  CompletableFuture<String> summarizeEbook(UUID conversationId, UUID userId);
}
