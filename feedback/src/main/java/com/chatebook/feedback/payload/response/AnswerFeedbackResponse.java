package com.chatebook.feedback.payload.response;

import com.chatebook.chat.payload.response.MessageInfoResponse;
import com.chatebook.security.payload.response.UserInfoResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerFeedbackResponse {

  private UUID id;

  private Timestamp createdAt;

  private String reasonFeedback;

  private String additionalFeedback;

  private Boolean isNegativeFeedback;

  private UserInfoResponse user;

  private MessageInfoResponse message;
}
