package com.chatebook.dashboard.service.impl;

import com.chatebook.dashboard.payload.response.InfoDashboardResponse;
import com.chatebook.dashboard.repository.DashboardRepository;
import com.chatebook.dashboard.service.DashboardService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

  private final DashboardRepository dashboardRepository;

  @Override
  public InfoDashboardResponse infoDashboard(LocalDateTime from, LocalDateTime to) {
    return dashboardRepository.getInfoDashboard(from, to);
  }
}
