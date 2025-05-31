package com.chatebook.mindmap.model;

import com.chatebook.chat.model.Conversation;
import com.chatebook.common.model.AbstractEntity;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "mind_maps")
public class MindMap extends AbstractEntity {

  @Id @GeneratedValue private UUID id;

  @Type(io.hypersistence.utils.hibernate.type.json.JsonType.class)
  @Column(columnDefinition = "jsonb")
  private JsonNode payload;

  @ManyToOne
  @JoinColumn(name = "conversation_id")
  private Conversation conversation;
}
