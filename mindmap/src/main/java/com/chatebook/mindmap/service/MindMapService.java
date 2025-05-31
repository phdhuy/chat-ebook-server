package com.chatebook.mindmap.service;

import com.chatebook.mindmap.model.MindMap;
import com.chatebook.mindmap.payload.response.MindMapInfoResponse;
import java.io.IOException;
import java.util.UUID;

public interface MindMapService {

  MindMapInfoResponse generateMindMap(UUID userId, UUID conversationId) throws IOException;

  MindMapInfoResponse getDetailMindMap(UUID userId, UUID mindMapId);

  MindMapInfoResponse getMindMapInConversation(UUID userId, UUID conversationId);

  MindMap findById(UUID id);
}
