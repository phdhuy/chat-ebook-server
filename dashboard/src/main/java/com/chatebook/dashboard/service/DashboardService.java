package com.chatebook.dashboard.service;

import com.chatebook.dashboard.payload.response.InfoDashboardResponse;
import java.time.LocalDateTime;

public interface DashboardService {

  InfoDashboardResponse infoDashboard(LocalDateTime from, LocalDateTime to);
}
