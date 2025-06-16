package com.chatebook.web.endpoint.admin;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.common.utils.PagingUtils;
import com.chatebook.feedback.service.AnswerFeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin Answer Feedback APIs")
public class AdminAnswerFeedbackController {

  private final AnswerFeedbackService answerFeedbackService;

  @GetMapping("/feedbacks")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseDataAPI getListAnswerFeedback(
      @RequestParam(name = "sort", defaultValue = "created_at") String sortBy,
      @RequestParam(name = "order", defaultValue = "asc") String order,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "paging", defaultValue = "30") int paging,
      @RequestParam(name = "query", required = false) String query) {
    Pageable pageable = PagingUtils.makePageRequestWithCamelCase(sortBy, order, page, paging);
    return answerFeedbackService.getAnswerFeedbackByAdmin(pageable, query);
  }
}
