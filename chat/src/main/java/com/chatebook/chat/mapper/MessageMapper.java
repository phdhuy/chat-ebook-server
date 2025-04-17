package com.chatebook.chat.mapper;

import com.chatebook.chat.model.Message;
import com.chatebook.chat.payload.response.MessageInfoResponse;
import com.chatebook.common.config.SpringMapStructConfig;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface MessageMapper {

  @Mapping(source = "message.id", target = "id")
  @Mapping(source = "message.createdAt", target = "createdAt")
  @Mapping(source = "conversationId", target = "conversationId")
  MessageInfoResponse toMessageInfoResponse(Message message, UUID conversationId);
}
