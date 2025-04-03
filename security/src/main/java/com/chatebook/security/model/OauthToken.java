package com.chatebook.security.model;

import com.chatebook.common.model.AbstractEntity;
import jakarta.persistence.*;
import java.sql.Timestamp;
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
@Entity(name = "oauth_tokens")
public class OauthToken extends AbstractEntity {

  @Id @GeneratedValue private UUID id;

  @Column private UUID refreshToken;

  @Column private Timestamp revokedAt;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
