package com.chatebook.common.ai.service.impl;

import com.chatebook.common.ai.payload.request.SendMessageToAIRequest;
import com.chatebook.common.ai.payload.request.SummarizeEbookRequest;
import com.chatebook.common.ai.payload.response.QueryAIResponse;
import com.chatebook.common.ai.payload.response.SummarizeEbookResponse;
import com.chatebook.common.ai.service.AIService;
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

  private static final String AI_BASE_URL = "http://rag-ai:5000";

  @Override
  @Async("asyncExecutor")
  public void uploadFile(MultipartFile file, UUID conversationId) {
    try {
      MediaType pdfMediaType = MediaType.parse("application/pdf");
      RequestBody fileBody = RequestBody.create(file.getBytes(), pdfMediaType);

      MultipartBody multipartBody =
          new MultipartBody.Builder()
              .setType(MultipartBody.FORM)
              .addFormDataPart("file", file.getOriginalFilename(), fileBody)
              .addFormDataPart("conversation_id", conversationId.toString())
              .build();

      Request httpRequest =
          new Request.Builder()
              .url(AI_BASE_URL + "/upload")
              .post(multipartBody)
              .addHeader("Accept", "application/json")
              .build();

      Response response = okHttpClient.newCall(httpRequest).execute();
      if (!response.isSuccessful() && response.body() != null) {
        String responseBody = response.body().string();
        throw new IOException("Upload failed: HTTP " + response.code() + " - " + responseBody);
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  @Async("asyncExecutor")
  public CompletableFuture<String> sendMessage(String content, UUID userId) {
    try {
      String json =
          objectMapper.writeValueAsString(SendMessageToAIRequest.builder().query(content).build());
      RequestBody body =
          RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

      Request httpRequest =
          new Request.Builder()
              .url(AI_BASE_URL + "/query")
              .post(body)
              .addHeader("Accept", "application/json")
              .build();

      try (Response response = okHttpClient.newCall(httpRequest).execute()) {
        if (response.isSuccessful() && response.body() != null) {
          String responseBody = response.body().string();
          QueryAIResponse queryAIResponse =
              objectMapper.readValue(responseBody, QueryAIResponse.class);
          String answer = queryAIResponse.getAnswer();
          log.info("AI response received for user {}: {}", userId, answer);
          return CompletableFuture.completedFuture(answer);
        } else {
          throw new IOException("AI service failed: HTTP " + response.code());
        }
      }
    } catch (IOException e) {
      log.error("Error sending message to AI service for user {}: {}", userId, e.getMessage());
      return CompletableFuture.failedFuture(e);
    } catch (Exception e) {
      log.error("Unexpected error in AI service communication: {}", e.getMessage());
      return CompletableFuture.failedFuture(e);
    }
  }

  @Override
  @Async("asyncExecutor")
  public CompletableFuture<String> summarizeEbook(UUID conversationId, UUID userId) {
    try {
      String json =
          objectMapper.writeValueAsString(SummarizeEbookRequest.builder().conversationId(conversationId.toString()).build());
      RequestBody body =
          RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

      Request httpRequest =
          new Request.Builder()
              .url(AI_BASE_URL + "/summarize")
              .post(body)
              .addHeader("Accept", "application/json")
              .build();

      try (Response response = okHttpClient.newCall(httpRequest).execute()) {
        if (response.isSuccessful() && response.body() != null) {
          String responseBody = response.body().string();
          SummarizeEbookResponse summarizeEbookResponse =
              objectMapper.readValue(responseBody, SummarizeEbookResponse.class);
          String summary = summarizeEbookResponse.getSummary();
          return CompletableFuture.completedFuture(summary);
        } else {
          throw new IOException("AI service failed: HTTP " + response.code());
        }
      }
    } catch (IOException e) {
      log.error("Error sending message to AI service for user {}: {}", userId, e.getMessage());
      return CompletableFuture.failedFuture(e);
    } catch (Exception e) {
      log.error("Unexpected error in AI service communication: {}", e.getMessage());
      return CompletableFuture.failedFuture(e);
    }
  }
}
