package com.chatebook.feedback.payload.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnswerFeedbackRequest {

  private Boolean isNegativeFeedback;

  private String reasonFeedback;

  private String additionalFeedback;
}
