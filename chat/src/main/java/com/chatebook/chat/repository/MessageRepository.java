package com.chatebook.chat.repository;

import com.chatebook.chat.model.Message;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  @Query(
      value =
          "SELECT DISTINCT m FROM messages m LEFT JOIN FETCH m.citedExcerpts WHERE m.conversation.id = :conversationId AND m.deletedAt IS NULL",
      countQuery =
          "SELECT COUNT(m) FROM messages m WHERE m.conversation.id = :conversationId AND m.deletedAt IS NULL")
  Page<Message> findAllByConversationId(Pageable pageable, UUID conversationId);
}
