package com.chatebook.chat.repository;

import com.chatebook.chat.model.Message;
import com.chatebook.chat.projection.MessageProjection;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  @Query(
      value =
          "SELECT m.id, m.created_at, m.content, m.sender_type, m.conversation_id, af.is_negative_feedback, "
              + "(SELECT json_agg(json_build_object("
              + "'id', ce.id, "
              + "'source_id', ce.source_id, "
              + "'text', ce.text, "
              + "'page', ce.page, "
              + "'score', ce.score)) "
              + "FROM cited_excerpts ce WHERE ce.message_id = m.id) AS cited_excerpts "
              + "FROM messages m "
              + "Left Join answer_feedbacks af on m.id = af.message_id "
              + "WHERE m.conversation_id = :conversationId AND m.deleted_at IS NULL ",
      countQuery =
          "SELECT COUNT(m.id) FROM messages m WHERE m.conversation_id = :conversationId AND m.deleted_at IS NULL",
      nativeQuery = true)
  Page<MessageProjection> findAllByConversationId(
      Pageable pageable, @Param("conversationId") UUID conversationId);
}
