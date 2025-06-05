package com.chatebook.web.endpoint.subscription;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.security.annotation.CurrentUser;
import com.chatebook.security.model.UserPrincipal;
import com.chatebook.subscription.service.ApiUsageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usages")
@Tag(name = "Api Usage APIs")
@Slf4j
public class ApiUsageController {

  private final ApiUsageService apiUsageService;

  @GetMapping("/count")
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI countMyApiUsage(
          @CurrentUser UserPrincipal userPrincipal,
          @RequestParam(required = false) LocalDateTime from,
          @RequestParam(required = false) LocalDateTime to,
          @RequestParam(required = false, defaultValue = "SUCCESS", name = "request_status") String requestStatus) {
    LocalDateTime start = (from != null) ? from : LocalDateTime.now().with(LocalTime.MIN);
    LocalDateTime end = (to != null) ? to : LocalDateTime.now().with(LocalTime.MAX);
    return apiUsageService.countApiUsage(userPrincipal.getId(), start, end, requestStatus);
  }
}
