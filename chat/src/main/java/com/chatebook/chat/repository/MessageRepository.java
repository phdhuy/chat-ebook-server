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
      "SELECT m from messages m WHERE m.conversation.id = :conversationId and m.deletedAt is null ORDER BY m.createdAt DESC")
  Page<Message> findAllByConversationId(Pageable pageable, UUID conversationId);
}
