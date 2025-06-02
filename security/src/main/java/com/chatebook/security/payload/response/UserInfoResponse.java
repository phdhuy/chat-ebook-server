package com.chatebook.security.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserInfoResponse {

  private UUID id;

  private String email;

  private Timestamp createdAt;

  private Timestamp deletedAt;

  private Timestamp confirmedAt;

  private Boolean isConfirmed;

  private String username;

  private String avatarUrl;
}
