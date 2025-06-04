package com.chatebook.subscription.repository;

import com.chatebook.subscription.model.ApiUsage;
import com.chatebook.subscription.payload.projection.CountApiUsageQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUsageRepository extends JpaRepository<ApiUsage, UUID> {

  @Query(
          value = """
            SELECT 
                rt.resource_type AS resource_type, 
                COALESCE(COUNT(au.resource_type), 0) AS count
            FROM (
                SELECT unnest(ARRAY[:resourceTypes]) AS resource_type
            ) rt
            LEFT JOIN api_usages au 
                ON rt.resource_type = au.resource_type 
                AND au.user_id = :userId 
                AND au.created_at >= :start 
                AND au.created_at < :end 
                AND au.request_status = :requestStatus
            GROUP BY rt.resource_type
            """,
          nativeQuery = true
  )
  List<CountApiUsageQuery> countApiUsageByResourceType(
          @Param("userId") UUID userId,
          @Param("start") LocalDateTime start,
          @Param("end") LocalDateTime end,
          @Param("requestStatus") String requestStatus,
          @Param("resourceTypes") String[] resourceTypes
  );
}
