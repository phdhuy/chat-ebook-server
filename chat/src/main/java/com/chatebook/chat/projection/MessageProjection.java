package com.chatebook.chat.projection;

import java.sql.Timestamp;
import java.util.UUID;

public interface MessageProjection {
  Long getId();

  Timestamp getCreatedAt();

  String getContent();

  String getSenderType();

  UUID getConversationId();

  Boolean getIsNegativeFeedback();

  String getCitedExcerpts();
}
