package com.chatebook.chat.mapper;

import com.chatebook.chat.model.Message;
import com.chatebook.chat.payload.response.MessageInfoResponse;
import com.chatebook.common.config.SpringMapStructConfig;
import com.chatebook.common.utils.EncryptionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.UUID;

@Mapper(config = SpringMapStructConfig.class, uses = { CitedExcerptMapper.class, EncryptionUtils.class })
public interface MessageMapper {

  @Mapping(source = "message.id", target = "id")
  @Mapping(source = "message.createdAt", target = "createdAt")
  @Mapping(source = "conversationId", target = "conversationId")
  @Mapping(source = "message.senderType", target = "senderType")
  @Mapping(source = "message.citedExcerpts", target = "citedExcerpts")
  @Mapping(target = "content", expression = "java(com.chatebook.common.utils.EncryptionUtils.decrypt(message.getContent()))")
  MessageInfoResponse toMessageInfoResponse(Message message, UUID conversationId);
}
