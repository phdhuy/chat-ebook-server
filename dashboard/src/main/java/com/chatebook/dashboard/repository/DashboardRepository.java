package com.chatebook.dashboard.repository;

import com.chatebook.dashboard.payload.response.InfoDashboardResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepository {

  @PersistenceContext private EntityManager entityManager;

  public InfoDashboardResponse getInfoDashboard(LocalDateTime from, LocalDateTime to) {
    Duration duration = Duration.between(from, to);
    LocalDateTime prevFrom = from.minus(duration);

    String sql =
            "SELECT\n"
                    + "  -- Total users created in the specified period\n"
                    + "  COALESCE((SELECT COUNT(*) FROM users WHERE created_at >= :from AND created_at < :to), 0) AS user_count,\n"
                    + "\n"
                    + "  -- Users active in the specified period\n"
                    + "  COALESCE((SELECT COUNT(*) FROM users WHERE is_confirmed = true), 0) AS active_user_count,\n"
                    + "\n"
                    + "  -- User growth %\n"
                    + "  COALESCE((\n"
                    + "    SELECT ROUND(\n"
                    + "      (\n"
                    + "        COUNT(*) FILTER (WHERE created_at >= :from AND created_at < :to)\n"
                    + "        - COUNT(*) FILTER (WHERE created_at >= :prevFrom AND created_at < :from)\n"
                    + "      ) * 100.0\n"
                    + "      / NULLIF(COUNT(*) FILTER (WHERE created_at >= :prevFrom AND created_at < :from), 0),\n"
                    + "      2\n"
                    + "    )\n"
                    + "    FROM users\n"
                    + "  ), 0) AS user_change_percent,\n"
                    + "\n"
                    + "  -- Document count and change\n"
                    + "  COALESCE((SELECT COUNT(*) FROM files WHERE created_at >= :from AND created_at < :to), 0) AS document_count,\n"
                    + "  COALESCE((\n"
                    + "    SELECT ROUND(\n"
                    + "      (\n"
                    + "        COUNT(*) FILTER (WHERE created_at >= :from AND created_at < :to)\n"
                    + "        - COUNT(*) FILTER (WHERE created_at >= :prevFrom AND created_at < :from)\n"
                    + "      ) * 100.0\n"
                    + "      / NULLIF(COUNT(*) FILTER (WHERE created_at >= :prevFrom AND created_at < :from), 0),\n"
                    + "      2\n"
                    + "    )\n"
                    + "    FROM files\n"
                    + "  ), 0) AS document_change_percent,\n"
                    + "\n"
                    + "  -- Conversation count and change\n"
                    + "  COALESCE((SELECT COUNT(*) FROM conversations WHERE created_at >= :from AND created_at < :to), 0) AS conversation_count,\n"
                    + "  COALESCE((\n"
                    + "    SELECT ROUND(\n"
                    + "      (\n"
                    + "        COUNT(*) FILTER (WHERE created_at >= :from AND created_at < :to)\n"
                    + "        - COUNT(*) FILTER (WHERE created_at >= :prevFrom AND created_at < :from)\n"
                    + "      ) * 100.0\n"
                    + "      / NULLIF(COUNT(*) FILTER (WHERE created_at >= :prevFrom AND created_at < :from), 0),\n"
                    + "      2\n"
                    + "    )\n"
                    + "    FROM conversations\n"
                    + "  ), 0) AS conversation_change_percent,\n"
                    + "\n"
                    + "  -- Feedback count and change\n"
                    + "  COALESCE((SELECT COUNT(*) FROM answer_feedbacks WHERE created_at >= :from AND created_at < :to), 0) AS feedback_count,\n"
                    + "  COALESCE((\n"
                    + "    SELECT ROUND(\n"
                    + "      (\n"
                    + "        COUNT(*) FILTER (WHERE created_at >= :from AND created_at < :to)\n"
                    + "        - COUNT(*) FILTER (WHERE created_at >= :prevFrom AND created_at < :from)\n"
                    + "      ) * 100.0\n"
                    + "      / NULLIF(COUNT(*) FILTER (WHERE created_at >= :prevFrom AND created_at < :from), 0),\n"
                    + "      2\n"
                    + "    )\n"
                    + "    FROM answer_feedbacks\n"
                    + "  ), 0) AS feedback_change_percent,\n"
                    + "\n"
                    + "  -- API call count and change\n"
                    + "  COALESCE((SELECT COUNT(*) FROM api_usages WHERE created_at >= :from AND created_at < :to), 0) AS api_count,\n"
                    + "  COALESCE((\n"
                    + "    SELECT ROUND(\n"
                    + "      (\n"
                    + "        COUNT(*) FILTER (WHERE created_at >= :from AND created_at < :to)\n"
                    + "        - COUNT(*) FILTER (WHERE created_at >= :prevFrom AND created_at < :from)\n"
                    + "      ) * 100.0\n"
                    + "      / NULLIF(COUNT(*) FILTER (WHERE created_at >= :prevFrom AND created_at < :from), 0),\n"
                    + "      2\n"
                    + "    )\n"
                    + "    FROM api_usages\n"
                    + "  ), 0) AS api_change_percent;\n";

    Query query = entityManager.createNativeQuery(sql);
    query.setParameter("from", from);
    query.setParameter("to", to);
    query.setParameter("prevFrom", prevFrom);

    Object[] result = (Object[]) query.getSingleResult();

    return InfoDashboardResponse.builder()
            .userCount(((Number) result[0]).longValue())
            .userActiveCount(((Number) result[1]).longValue())
            .userChangePercent(((Number) result[2]).doubleValue())
            .documentCount(((Number) result[3]).longValue())
            .documentChangePercent(((Number) result[4]).doubleValue())
            .conversationCount(((Number) result[5]).longValue())
            .conversationChangePercent(((Number) result[6]).doubleValue())
            .feedbackCount(((Number) result[7]).longValue())
            .feedbackChangePercent(((Number) result[8]).doubleValue())
            .apiUsageCount(((Number) result[9]).longValue())
            .apiUsageChangePercent(((Number) result[10]).doubleValue())
            .build();
  }
}
