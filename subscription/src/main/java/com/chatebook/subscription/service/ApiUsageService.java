package com.chatebook.subscription.service;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.subscription.model.enums.RequestStatus;
import com.chatebook.subscription.model.enums.ResourceType;
import java.time.LocalDateTime;
import java.util.UUID;

public interface ApiUsageService {

  void save(ResourceType resourceType, RequestStatus requestStatus, UUID userId);

  ResponseDataAPI countApiUsage(UUID userId, LocalDateTime start, LocalDateTime end, String requestStatus);
}
