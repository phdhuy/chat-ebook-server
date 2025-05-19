package com.chatebook.common.ai.service.impl;

import com.chatebook.common.ai.payload.request.SendMessageToAIRequest;
import com.chatebook.common.ai.payload.request.SummarizeEbookRequest;
import com.chatebook.common.ai.payload.response.QueryAIResponse;
import com.chatebook.common.ai.payload.response.SummarizeEbookResponse;
import com.chatebook.common.ai.service.AIService;
import com.chatebook.common.constant.CommonConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIServiceImpl implements AIService {

  private final OkHttpClient okHttpClient;

  private final ObjectMapper objectMapper;

  @Override
  @Async("asyncExecutor")
  public void uploadFile(MultipartFile file, UUID conversationId) {
    try {
      RequestBody fileBody = RequestBody.create(file.getBytes(), CommonConstant.PDF_MEDIA_TYPE);
      MultipartBody multipartBody =
          new MultipartBody.Builder()
              .setType(MultipartBody.FORM)
              .addFormDataPart("file", file.getOriginalFilename(), fileBody)
              .addFormDataPart("conversation_id", conversationId.toString())
              .build();

      Request request = buildRequest("/upload", multipartBody);
      executeRequest(request, "File upload failed");
    } catch (IOException e) {
      log.error("Failed to upload file for conversation {}: {}", conversationId, e.getMessage());
    }
  }

  @Override
  @Async("asyncExecutor")
  public CompletableFuture<String> sendMessage(String content, UUID userId) {
    try {
      String json =
          objectMapper.writeValueAsString(SendMessageToAIRequest.builder().query(content).build());
      RequestBody body = RequestBody.create(json, CommonConstant.JSON_MEDIA_TYPE);
      Request request = buildRequest("/query", body);

      return executeRequestForResponse(request, QueryAIResponse.class, userId)
          .thenApply(QueryAIResponse::getAnswer);
    } catch (IOException e) {
      log.error("Error sending message to AI service for user {}: {}", userId, e.getMessage());
      return CompletableFuture.failedFuture(e);
    }
  }

  @Override
  @Async("asyncExecutor")
  public CompletableFuture<String> summarizeEbook(UUID conversationId, UUID userId) {
    try {
      String json =
          objectMapper.writeValueAsString(
              SummarizeEbookRequest.builder().conversationId(conversationId.toString()).build());
      RequestBody body = RequestBody.create(json, CommonConstant.JSON_MEDIA_TYPE);
      Request request = buildRequest("/summarize", body);

      return executeRequestForResponse(request, SummarizeEbookResponse.class, userId)
          .thenApply(SummarizeEbookResponse::getSummary);
    } catch (IOException e) {
      log.error("Error summarizing ebook for user {}: {}", userId, e.getMessage());
      return CompletableFuture.failedFuture(e);
    }
  }

  private Request buildRequest(String endpoint, RequestBody body) {
    return new Request.Builder()
        .url(CommonConstant.AI_BASE_URL + endpoint)
        .post(body)
        .addHeader("Accept", "application/json")
        .build();
  }

  private void executeRequest(Request request, String errorMessage) throws IOException {
    try (Response response = okHttpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        String responseBody =
            response.body() != null ? response.body().string() : "No response body";
        throw new IOException(
            String.format("%s: HTTP %d - %s", errorMessage, response.code(), responseBody));
      }
    }
  }

  private <T> CompletableFuture<T> executeRequestForResponse(
      Request request, Class<T> responseType, UUID userId) {
    return CompletableFuture.supplyAsync(
        () -> {
          try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
              String responseBody = response.body().string();
              T result = objectMapper.readValue(responseBody, responseType);
              log.info("AI response received for user {}: {}", userId, result);
              return result;
            } else {
              throw new IOException("AI service failed: HTTP " + response.code());
            }
          } catch (IOException e) {
            throw new RuntimeException("Failed to process AI response for user " + userId, e);
          }
        });
  }
}
