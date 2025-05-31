package com.chatebook.mindmap.repository;

import com.chatebook.mindmap.model.MindMap;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MindMapRepository extends JpaRepository<MindMap, UUID> {

  boolean existsMindMapByConversationIdAndDeletedAtIsNull(UUID conversationId);

  MindMap findMindMapByConversationIdAndDeletedAtIsNull(UUID conversationId);
}
