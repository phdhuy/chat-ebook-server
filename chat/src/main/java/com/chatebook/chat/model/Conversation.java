package com.chatebook.chat.model;

import com.chatebook.common.model.AbstractEntity;
import com.chatebook.file.model.File;
import com.chatebook.security.model.User;
import jakarta.persistence.*;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "conversations")
public class Conversation extends AbstractEntity {

  @Id @GeneratedValue private UUID id;

  @Column private String name;

  @OneToOne
  @JoinColumn(name = "file_id")
  private File file;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
