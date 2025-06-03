package com.chatebook.subscription.model;

import com.chatebook.common.model.AbstractEntity;
import com.chatebook.security.model.User;
import com.chatebook.subscription.model.enums.RequestStatus;
import com.chatebook.subscription.model.enums.ResourceType;
import jakarta.persistence.*;
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
@Entity(name = "api_usages")
public class ApiUsage extends AbstractEntity {

  @Id @GeneratedValue private UUID id;

  @Column
  @Enumerated(EnumType.STRING)
  private ResourceType resourceType;

  @Column
  @Enumerated(EnumType.STRING)
  private RequestStatus requestStatus;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
