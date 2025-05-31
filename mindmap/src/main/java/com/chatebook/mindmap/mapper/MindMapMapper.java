package com.chatebook.mindmap.mapper;

import com.chatebook.common.config.SpringMapStructConfig;
import com.chatebook.mindmap.model.MindMap;
import com.chatebook.mindmap.payload.response.MindMapInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface MindMapMapper {

  @Mapping(source = "mindMap.conversation.id", target = "conversationId")
  @Mapping(source = "mindMap.payload", target = "payload")
  MindMapInfoResponse toMindMapInfoResponse(MindMap mindMap);
}
