package com.chatebook.mindmap.service.impl;

import com.chatebook.ai.payload.response.GenerateMindMapResponse;
import com.chatebook.ai.service.AIService;
import com.chatebook.chat.model.Conversation;
import com.chatebook.chat.service.ConversationService;
import com.chatebook.common.common.CommonFunction;
import com.chatebook.common.constant.MessageConstant;
import com.chatebook.common.exception.NotFoundException;
import com.chatebook.mindmap.mapper.MindMapMapper;
import com.chatebook.mindmap.model.MindMap;
import com.chatebook.mindmap.payload.response.MindMapInfoResponse;
import com.chatebook.mindmap.repository.MindMapRepository;
import com.chatebook.mindmap.service.MindMapService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MindMapServiceImpl implements MindMapService {

  private final MindMapRepository mindMapRepository;

  private final ConversationService conversationService;

  private final AIService aiService;

  private final ObjectMapper objectMapper;

  private final MindMapMapper mindMapMapper;

  @Override
  public MindMapInfoResponse generateMindMap(UUID userId, UUID conversationId) throws IOException {
    Conversation conversation =
        conversationService.checkConversationOwnership(conversationId, userId);

    if (!this.isExistMindMapInConversation(conversationId)) {
      GenerateMindMapResponse mindMapResponse =
          aiService.generateMindMap(
              CommonFunction.convertUrlToMultipartFile(
                  conversation.getFile().getSecureUrl(), conversation.getName()),
              conversationId);

      JsonNode payload = objectMapper.valueToTree(mindMapResponse);

      MindMap mindMap = new MindMap();
      mindMap.setPayload(payload);
      mindMap.setConversation(conversation);

      mindMapRepository.save(mindMap);

      return mindMapMapper.toMindMapInfoResponse(mindMap);
    } else {
      MindMap mindMap =
          mindMapRepository.findMindMapByConversationIdAndDeletedAtIsNull(conversationId);
      return mindMapMapper.toMindMapInfoResponse(mindMap);
    }
  }

  @Override
  public MindMapInfoResponse getDetailMindMap(UUID userId, UUID mindMapId) {
    MindMap mindMap = this.findById(mindMapId);
    return mindMapMapper.toMindMapInfoResponse(mindMap);
  }

  @Override
  public MindMapInfoResponse getMindMapInConversation(UUID userId, UUID conversationId) {
    MindMap mindMap =
        mindMapRepository.findMindMapByConversationIdAndDeletedAtIsNull(conversationId);
    return mindMapMapper.toMindMapInfoResponse(mindMap);
  }

  @Override
  public MindMap findById(UUID id) {
    return mindMapRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MessageConstant.MINDMAP_NOT_FOUND));
  }

  private boolean isExistMindMapInConversation(UUID conversationId) {
    return mindMapRepository.existsMindMapByConversationIdAndDeletedAtIsNull(conversationId);
  }
}
