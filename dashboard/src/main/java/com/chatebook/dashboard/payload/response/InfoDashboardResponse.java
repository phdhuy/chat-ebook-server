package com.chatebook.dashboard.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class InfoDashboardResponse {

  private Long userCount;

  private Long userActiveCount;

  private Double userChangePercent;

  private Long documentCount;

  private Double documentChangePercent;

  private Long conversationCount;

  private Double conversationChangePercent;

  private Long feedbackCount;

  private Double feedbackChangePercent;

  private Long apiUsageCount;

  private Double apiUsageChangePercent;
}
