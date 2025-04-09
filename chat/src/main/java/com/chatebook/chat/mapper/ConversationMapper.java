package com.chatebook.chat.mapper;

import com.chatebook.chat.model.Conversation;
import com.chatebook.chat.payload.response.ConversationInfoResponse;
import com.chatebook.common.config.SpringMapStructConfig;
import com.chatebook.file.payload.response.FileInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface ConversationMapper {

  @Mapping(source = "conversation.id", target = "id")
  @Mapping(source = "conversation.createdAt", target = "createdAt")
  @Mapping(source = "conversation.name", target = "name")
  @Mapping(source = "fileInfoResponse", target = "file")
  ConversationInfoResponse toConversationInfoResponse(
      Conversation conversation, FileInfoResponse fileInfoResponse);
}
