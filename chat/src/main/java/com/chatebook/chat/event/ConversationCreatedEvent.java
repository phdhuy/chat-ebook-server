package com.chatebook.chat.event;

import com.chatebook.ai.payload.response.UploadFileAIResponse;
import com.chatebook.chat.model.Conversation;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ConversationCreatedEvent extends ApplicationEvent {

  private final Conversation conversation;

  private final UUID userId;

  private final UploadFileAIResponse uploadResponse;

  public ConversationCreatedEvent(
      Object source, Conversation conversation, UploadFileAIResponse uploadResponse, UUID userId) {
    super(source);
    this.conversation = conversation;
    this.uploadResponse = uploadResponse;
    this.userId = userId;
  }
}
