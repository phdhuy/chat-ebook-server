package com.chatebook.chat.utils;

import com.chatebook.chat.payload.response.CitedExcerptInfoResponse;
import com.chatebook.chat.payload.response.MessageInfoResponse;
import com.chatebook.chat.projection.MessageProjection;
import com.chatebook.common.exception.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageUtils {

  private final ObjectMapper objectMapper;

  public MessageInfoResponse toMessageInfoResponse(MessageProjection messageProjection) {
    MessageInfoResponse response = new MessageInfoResponse();
    response.setId(messageProjection.getId());
    response.setCreatedAt(messageProjection.getCreatedAt());
    response.setContent(messageProjection.getContent());
    response.setSenderType(messageProjection.getSenderType());
    response.setConversationId(messageProjection.getConversationId());
    response.setIsNegativeFeedback(messageProjection.getIsNegativeFeedback());

    List<CitedExcerptInfoResponse> excerpts = new ArrayList<>();
    if (messageProjection.getCitedExcerpts() != null
        && !messageProjection.getCitedExcerpts().equals("[]")) {
      try {
        JsonNode excerptsJson = objectMapper.readTree(messageProjection.getCitedExcerpts());
        for (JsonNode node : excerptsJson) {
          CitedExcerptInfoResponse excerpt =
              CitedExcerptInfoResponse.builder()
                  .id(UUID.fromString(node.get("id").asText()))
                  .sourceId(node.get("source_id").asInt())
                  .text(node.get("text").asText())
                  .page(node.get("page").asInt())
                  .score(node.get("score").asDouble())
                  .build();
          excerpts.add(excerpt);
        }
      } catch (JsonProcessingException e) {
        throw new BadRequestException("Failed to parse cited excerpts JSON");
      }
    }
    response.setCitedExcerpts(excerpts);

    return response;
  }
}
