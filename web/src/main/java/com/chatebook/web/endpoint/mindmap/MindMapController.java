package com.chatebook.web.endpoint.mindmap;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.mindmap.service.MindMapService;
import com.chatebook.security.annotation.CurrentUser;
import com.chatebook.security.model.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@Tag(name = "MindMap APIs")
public class MindMapController {

  private final MindMapService mindMapService;

  @PostMapping("conversations/{conversationId}/mindmaps")
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI createMessage(
      @CurrentUser UserPrincipal userPrincipal, @PathVariable UUID conversationId)
      throws IOException {
    return ResponseDataAPI.successWithoutMeta(
        mindMapService.generateMindMap(userPrincipal.getId(), conversationId));
  }

  @GetMapping("conversations/{conversationId}/mindmaps")
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI getMindMapInConversation(
      @CurrentUser UserPrincipal userPrincipal, @PathVariable UUID conversationId) {
    return ResponseDataAPI.successWithoutMeta(
        mindMapService.getMindMapInConversation(userPrincipal.getId(), conversationId));
  }

  @GetMapping("/mindmaps/{mindMapId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI getDetailMindMap(
      @CurrentUser UserPrincipal userPrincipal, @PathVariable UUID mindMapId) {
    return ResponseDataAPI.successWithoutMeta(
        mindMapService.getDetailMindMap(userPrincipal.getId(), mindMapId));
  }
}
