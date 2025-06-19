package com.chatebook.feedback.repository;

import com.chatebook.feedback.model.AnswerFeedback;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerFeedbackRepository extends JpaRepository<AnswerFeedback, UUID> {

  @Query(
      value =
          """
      SELECT a
        FROM answer_feedbacks a
        left JOIN FETCH a.user u
        left JOIN FETCH a.message m
        left Join fetch m.conversation c
        left join fetch c.file f
        left join fetch m.citedExcerpts ce
       WHERE LOWER(a.reasonFeedback)     LIKE LOWER(CONCAT('%',:query,'%'))
          OR LOWER(a.additionalFeedback) LIKE LOWER(CONCAT('%',:query,'%'))
      """,
      countQuery =
          """
      SELECT COUNT(a)
        FROM answer_feedbacks a
       WHERE LOWER(a.reasonFeedback)     LIKE LOWER(CONCAT('%',:query,'%'))
          OR LOWER(a.additionalFeedback) LIKE LOWER(CONCAT('%',:query,'%'))
      """)
  Page<AnswerFeedback> getListAnswerFeedbackByAdmin(Pageable pageable, String query);

  boolean existsAnswerFeedbackByMessageId(Long messageId);
}
