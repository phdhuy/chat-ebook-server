package com.chatebook.chat.model;

import com.chatebook.chat.model.enums.SenderType;
import com.chatebook.common.model.AbstractEntity;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "messages")
public class Message extends AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  @Column
  @Enumerated(EnumType.STRING)
  private SenderType senderType;

  @ManyToOne
  @JoinColumn(name = "conversation_id")
  private Conversation conversation;

  @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CitedExcerpt> citedExcerpts;
}
