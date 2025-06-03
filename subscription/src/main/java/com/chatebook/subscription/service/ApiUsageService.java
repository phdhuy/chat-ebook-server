package com.chatebook.subscription.service;

import com.chatebook.subscription.model.enums.RequestStatus;
import com.chatebook.subscription.model.enums.ResourceType;
import java.util.UUID;

public interface ApiUsageService {

  void save(ResourceType resourceType, RequestStatus requestStatus, UUID userId);
}
