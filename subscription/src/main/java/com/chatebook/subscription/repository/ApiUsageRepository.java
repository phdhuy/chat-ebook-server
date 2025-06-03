package com.chatebook.subscription.repository;

import com.chatebook.subscription.model.ApiUsage;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUsageRepository extends JpaRepository<ApiUsage, UUID> {}
