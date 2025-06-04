package com.chatebook.subscription.service.impl;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.security.repository.UserRepository;
import com.chatebook.subscription.model.ApiUsage;
import com.chatebook.subscription.model.enums.RequestStatus;
import com.chatebook.subscription.model.enums.ResourceType;
import com.chatebook.subscription.repository.ApiUsageRepository;
import com.chatebook.subscription.service.ApiUsageService;
import java.time.LocalDateTime;
import java.util.Arrays;
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

  @Override
  public ResponseDataAPI countApiUsage(UUID userId, LocalDateTime start, LocalDateTime end, String requestStatus) {
    String[] resourceTypes = Arrays.stream(ResourceType.values())
            .map(Enum::name)
            .toArray(String[]::new);
    return ResponseDataAPI.successWithoutMeta(
        apiUsageRepository.countApiUsageByResourceType(userId, start, end, requestStatus, resourceTypes));
  }
}
