package com.chatebook.feedback.mapper;

import com.chatebook.chat.payload.response.MessageInfoResponse;
import com.chatebook.common.config.SpringMapStructConfig;
import com.chatebook.feedback.model.AnswerFeedback;
import com.chatebook.feedback.payload.response.AnswerFeedbackResponse;
import com.chatebook.security.payload.response.UserInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface AnswerFeedbackMapper {

  @Mapping(target = "user", source = "user")
  @Mapping(target = "message", source = "message")
  @Mapping(target = "id", source = "answerFeedback.id")
  @Mapping(target = "createdAt", source = "answerFeedback.createdAt")
  @Mapping(target = "reasonFeedback", source = "answerFeedback.reasonFeedback")
  @Mapping(target = "additionalFeedback", source = "answerFeedback.additionalFeedback")
  AnswerFeedbackResponse toAnswerFeedbackResponse(
      AnswerFeedback answerFeedback, UserInfoResponse user, MessageInfoResponse message);
}
