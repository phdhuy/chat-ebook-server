package com.chatebook.chat.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "cited_excerpts")
@Builder
public class CitedExcerpt {

  @Id @GeneratedValue private UUID id;

  @Column(name = "source_id", nullable = false)
  private Integer sourceId;

  @Column(nullable = false)
  private Integer page;

  @Column(nullable = false)
  private Double score;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String text;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "message_id", nullable = false)
  private Message message;
}
