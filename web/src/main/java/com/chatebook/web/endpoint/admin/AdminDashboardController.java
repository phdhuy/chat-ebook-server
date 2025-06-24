package com.chatebook.web.endpoint.admin;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.dashboard.service.DashboardService;
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
@RequestMapping("/api/v1/admin/dashboards")
@Tag(name = "Admin Dashboard APIs")
public class AdminDashboardController {

  private final DashboardService dashboardService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseDataAPI dashboard(
      @RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
    return ResponseDataAPI.successWithoutMeta(dashboardService.infoDashboard(from, to));
  }
}
