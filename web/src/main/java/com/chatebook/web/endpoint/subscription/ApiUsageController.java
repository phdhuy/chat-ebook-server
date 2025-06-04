package com.chatebook.web.endpoint.subscription;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.common.utils.DateRangeUtils;
import com.chatebook.security.annotation.CurrentUser;
import com.chatebook.security.model.UserPrincipal;
import com.chatebook.subscription.service.ApiUsageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usages")
@Tag(name = "Api Usage APIs")
public class ApiUsageController {

  private final ApiUsageService apiUsageService;

  @GetMapping("/count")
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI countMyApiUsage(
      @CurrentUser UserPrincipal userPrincipal,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month,
      @RequestParam(required = false) Integer day,
      @RequestParam(required = false, defaultValue = "SUCCESS", name = "request_status")
          String requestStatus) {
    try {
      DateRangeUtils.DateRange range = DateRangeUtils.getDateRange(day, month, year);
      LocalDateTime start = range.start();
      LocalDateTime end = range.end();
      return apiUsageService.countApiUsage(userPrincipal.getId(), start, end, requestStatus);
    } catch (IllegalArgumentException e) {
      return ResponseDataAPI.error(e.getMessage());
    }
  }
}
