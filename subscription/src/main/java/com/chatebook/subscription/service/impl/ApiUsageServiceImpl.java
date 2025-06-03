package com.chatebook.subscription.service.impl;

import com.chatebook.security.repository.UserRepository;
import com.chatebook.subscription.model.ApiUsage;
import com.chatebook.subscription.model.enums.RequestStatus;
import com.chatebook.subscription.model.enums.ResourceType;
import com.chatebook.subscription.repository.ApiUsageRepository;
import com.chatebook.subscription.service.ApiUsageService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiUsageServiceImpl implements ApiUsageService {

  private final ApiUsageRepository apiUsageRepository;

  private final UserRepository userRepository;

  @Override
  public void save(ResourceType resourceType, RequestStatus requestStatus, UUID userId) {
    var apiUsage = new ApiUsage();
    apiUsage.setResourceType(resourceType);
    apiUsage.setRequestStatus(requestStatus);
    apiUsage.setUser(userRepository.getReferenceById(userId));
    apiUsageRepository.save(apiUsage);
  }
}
