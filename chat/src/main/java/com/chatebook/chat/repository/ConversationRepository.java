package com.chatebook.chat.repository;

import com.chatebook.chat.model.Conversation;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

  @Query(
      value =
          "SELECT c FROM conversations c LEFT JOIN FETCH c.file WHERE c.user.id = :userId and c.deletedAt is null ORDER BY COALESCE(c.isFavorite, FALSE) DESC, c.createdAt DESC")
  Page<Conversation> getMyConversations(Pageable pageable, UUID userId);

  @Query(
      value = "SELECT * FROM conversations c WHERE c.name ILIKE CONCAT('%', :query, '%')",
      nativeQuery = true)
  Page<Conversation> getListConversationByAdmin(Pageable pageable, String query);
}
