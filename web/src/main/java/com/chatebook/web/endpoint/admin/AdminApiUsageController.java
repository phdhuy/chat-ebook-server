package com.chatebook.web.endpoint.admin;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.subscription.service.ApiUsageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin Usage APIs")
public class AdminApiUsageController {

  private final ApiUsageService apiUsageService;

  @GetMapping("/users/{userId}/api-usage")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseDataAPI getApiUsageByUserId(
      @PathVariable UUID userId,
      @RequestParam(required = false) LocalDateTime from,
      @RequestParam(required = false) LocalDateTime to,
      @RequestParam(required = false, defaultValue = "SUCCESS", name = "request_status")
          String requestStatus) {
    LocalDateTime start = (from != null) ? from : LocalDateTime.now().with(LocalTime.MIN);
    LocalDateTime end = (to != null) ? to : LocalDateTime.now().with(LocalTime.MAX);
    return apiUsageService.countApiUsage(userId, start, end, requestStatus);
  }
}
