package com.chatebook.security.model;

import com.chatebook.common.model.AbstractEntity;
import com.chatebook.common.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table
@Entity(name = "users")
public class User extends AbstractEntity {

  @Id @GeneratedValue private UUID id;

  @Email @Column private String email;

  @Column private String password;

  @Column
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column private UUID confirmationToken;

  @Column private Timestamp confirmedAt;

  @Column private UUID resetPasswordToken;

  @Column private Timestamp resetPasswordSentAt;

  @Column(nullable = false)
  private Boolean isConfirmed = false;
}
