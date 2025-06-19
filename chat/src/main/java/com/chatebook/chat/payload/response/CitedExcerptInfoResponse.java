package com.chatebook.chat.payload.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CitedExcerptInfoResponse {

  private UUID id;

  private Integer sourceId;

  private String text;

  private Integer page;

  private Double score;
}
