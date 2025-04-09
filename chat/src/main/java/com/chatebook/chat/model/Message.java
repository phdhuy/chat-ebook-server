package com.chatebook.chat.model;

import com.chatebook.chat.model.enums.SenderType;
import com.chatebook.common.model.AbstractEntity;
import jakarta.persistence.*;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
  @SequenceGenerator(name = "message_seq", sequenceName = "messages_seq", allocationSize = 50)
  private Long id;

  @Column private String content;

  @Column
  @Enumerated(EnumType.STRING)
  private SenderType senderType;

  @ManyToOne
  @JoinColumn(name = "converation_id")
  private Conversation conversation;
}
