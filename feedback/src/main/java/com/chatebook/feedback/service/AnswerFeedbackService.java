package com.chatebook.feedback.service;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.feedback.payload.request.AnswerFeedbackRequest;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface AnswerFeedbackService {

  void createAnswerFeedback(
      UUID userId, Long messageId, AnswerFeedbackRequest answerFeedbackRequest);

  ResponseDataAPI getAnswerFeedbackByAdmin(Pageable pageable, String query);
}
