package com.chatebook.common.ai.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

public interface AIService {

  void uploadFile(MultipartFile file);

  CompletableFuture<String> sendMessage(String content, UUID userId);
}
