package com.chatebook.ai.service.impl;

import com.chatebook.ai.payload.request.SendMessageToAIRequest;
import com.chatebook.ai.payload.request.SummarizeEbookRequest;
import com.chatebook.ai.payload.response.GenerateMindMapResponse;
import com.chatebook.ai.payload.response.QueryAIResponse;
import com.chatebook.ai.payload.response.SummarizeEbookResponse;
import com.chatebook.ai.payload.response.UploadFileAIResponse;
import com.chatebook.ai.service.AIService;
import com.chatebook.common.constant.CommonConstant;
import com.chatebook.subscription.model.enums.RequestStatus;
import com.chatebook.subscription.model.enums.ResourceType;
import com.chatebook.subscription.service.ApiUsageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
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

  private final ApiUsageService apiUsageService;

  private final ObjectMapper objectMapper;

  @Override
  public UploadFileAIResponse uploadFile(MultipartFile file, UUID conversationId, UUID userId)
      throws IOException {
    try {
      RequestBody fileBody = RequestBody.create(file.getBytes(), CommonConstant.PDF_MEDIA_TYPE);
      MultipartBody multipartBody =
          new MultipartBody.Builder()
              .setType(MultipartBody.FORM)
              .addFormDataPart("file", file.getOriginalFilename(), fileBody)
              .addFormDataPart("conversation_id", conversationId.toString())
              .build();

      Request request = buildRequest("/upload", multipartBody);

      UploadFileAIResponse response = executeRequestForResponseSync(request);
      apiUsageService.save(ResourceType.UPLOAD_EBOOK, RequestStatus.SUCCESS, userId);
      return response;
    } catch (IOException e) {
      log.error("Failed to upload file for conversation {}: {}", conversationId, e.getMessage());
      apiUsageService.save(ResourceType.UPLOAD_EBOOK, RequestStatus.FAILED, userId);
      throw e;
    }
  }

  @Override
  @Async("asyncExecutor")
  public CompletableFuture<QueryAIResponse> sendMessage(
      String content, String historyConversation, UUID userId, UUID conversationId) {
    try {
      String json =
          objectMapper.writeValueAsString(
              SendMessageToAIRequest.builder()
                  .query(content)
                  .history(historyConversation)
                  .conversationId(conversationId.toString())
                  .build());
      RequestBody body = RequestBody.create(json, CommonConstant.JSON_MEDIA_TYPE);
      Request request = buildRequest("/query", body);

      return executeRequestForResponse(request, QueryAIResponse.class)
          .thenApply(
              response -> {
                apiUsageService.save(ResourceType.SEND_MESSAGE, RequestStatus.SUCCESS, userId);
                return response;
              })
          .exceptionally(
              ex -> {
                log.error(
                    "Error sending message to AI service for user {}: {}", userId, ex.getMessage());
                apiUsageService.save(ResourceType.SEND_MESSAGE, RequestStatus.FAILED, userId);
                throw new CompletionException(ex);
              });
    } catch (IOException e) {
      log.error(
          "Error preparing request for sending message to AI service for user {}: {}",
          userId,
          e.getMessage());
      apiUsageService.save(ResourceType.SEND_MESSAGE, RequestStatus.FAILED, userId);
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

      return executeRequestForResponse(request, SummarizeEbookResponse.class)
          .thenApply(
              response -> {
                apiUsageService.save(ResourceType.SUMMARIZE_EBOOK, RequestStatus.SUCCESS, userId);
                return response.getSummary();
              })
          .exceptionally(
              ex -> {
                log.error("Error summarizing ebook for user {}: {}", userId, ex.getMessage());
                apiUsageService.save(ResourceType.SUMMARIZE_EBOOK, RequestStatus.FAILED, userId);
                throw new CompletionException(ex);
              });
    } catch (IOException e) {
      log.error(
          "Error preparing request for summarizing ebook for user {}: {}", userId, e.getMessage());
      apiUsageService.save(ResourceType.SUMMARIZE_EBOOK, RequestStatus.FAILED, userId);
      return CompletableFuture.failedFuture(e);
    }
  }

  @Override
  public GenerateMindMapResponse generateMindMap(
      MultipartFile file, UUID conversationId, UUID userId) {
    try {
      RequestBody fileBody = RequestBody.create(file.getBytes(), CommonConstant.PDF_MEDIA_TYPE);
      MultipartBody multipartBody =
          new MultipartBody.Builder()
              .setType(MultipartBody.FORM)
              .addFormDataPart("file", file.getOriginalFilename(), fileBody)
              .build();

      Request request = buildRequest("/mindmap", multipartBody);

      try (Response response = okHttpClient.newCall(request).execute()) {
        if (response.isSuccessful() && response.body() != null) {
          String responseJson = response.body().string();
          GenerateMindMapResponse result =
              objectMapper.readValue(responseJson, GenerateMindMapResponse.class);
          apiUsageService.save(ResourceType.GENERATE_MIND_MAP, RequestStatus.SUCCESS, userId);
          return result;
        } else {
          String message =
              "AI service returned HTTP "
                  + response.code()
                  + " (body="
                  + (response.body() == null ? "null" : response.body().string())
                  + ")";
          log.error("Failed to generate mind map for conversation {}: {}", conversationId, message);
          apiUsageService.save(ResourceType.GENERATE_MIND_MAP, RequestStatus.FAILED, userId);
          throw new IOException(message);
        }
      }
    } catch (IOException e) {
      log.error("Error in generateMindMap for conversation {}: {}", conversationId, e.getMessage());
      apiUsageService.save(ResourceType.GENERATE_MIND_MAP, RequestStatus.FAILED, userId);
      throw new RuntimeException("Unable to generate mind map", e);
    }
  }

  private Request buildRequest(String endpoint, RequestBody body) {
    return new Request.Builder()
        .url(CommonConstant.AI_BASE_URL + endpoint)
        .post(body)
        .addHeader("Accept", "application/json")
        .build();
  }

  private <T> CompletableFuture<T> executeRequestForResponse(
      Request request, Class<T> responseType) {
    return CompletableFuture.supplyAsync(
        () -> {
          try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
              String responseBody = response.body().string();
              T result = objectMapper.readValue(responseBody, responseType);
              log.info("Deserialized result: {}", objectMapper.writeValueAsString(result));
              return result;
            } else {
              throw new IOException("AI service failed: HTTP " + response.code());
            }
          } catch (IOException e) {
            throw new RuntimeException("Failed to process AI response for user " + e);
          }
        });
  }

  private UploadFileAIResponse executeRequestForResponseSync(Request request) throws IOException {
    try (Response response = okHttpClient.newCall(request).execute()) {
      if (!response.isSuccessful() || response.body() == null) {
        throw new IOException("Unexpected code " + response);
      }
      String responseBody = response.body().string();
      return objectMapper.readValue(responseBody, UploadFileAIResponse.class);
    }
  }
}
