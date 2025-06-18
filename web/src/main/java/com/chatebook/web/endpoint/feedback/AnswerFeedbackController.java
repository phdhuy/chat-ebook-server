package com.chatebook.web.endpoint.feedback;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.feedback.payload.request.AnswerFeedbackRequest;
import com.chatebook.feedback.service.AnswerFeedbackService;
import com.chatebook.security.annotation.CurrentUser;
import com.chatebook.security.model.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Answer Feedback APIs")
public class AnswerFeedbackController {

  private final AnswerFeedbackService answerFeedbackService;

  @PostMapping("/messages/{messageId}/feedbacks")
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI createAnswerFeedback(
      @CurrentUser UserPrincipal userPrincipal,
      @PathVariable Long messageId,
      @RequestBody @Valid AnswerFeedbackRequest answerFeedbackRequest) {
    answerFeedbackService.createAnswerFeedback(
        userPrincipal.getId(), messageId, answerFeedbackRequest);
    return ResponseDataAPI.successWithoutMetaAndData();
  }
}
