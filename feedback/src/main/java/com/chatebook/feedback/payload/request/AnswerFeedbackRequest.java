package com.chatebook.feedback.payload.request;

import com.chatebook.feedback.model.enums.ReasonFeedback;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnswerFeedbackRequest {

  @NotBlank private String reasonFeedback;

  private String additionalFeedback;
}
