package com.chatebook.web.endpoint.chat;

import com.chatebook.chat.service.ConversationService;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.common.utils.PagingUtils;
import com.chatebook.security.annotation.CurrentUser;
import com.chatebook.security.model.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversations")
@Tag(name = "Conversation APIs")
public class ConversationController {

  private final ConversationService conversationService;

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> createConversation(
      @CurrentUser UserPrincipal userPrincipal, @RequestParam(value = "file") MultipartFile file) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            conversationService.createConversation(userPrincipal.getId(), file)));
  }

  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> getMyConversations(
      @CurrentUser UserPrincipal userPrincipal,
      @RequestParam(name = "sort", defaultValue = "created_at") String sortBy,
      @RequestParam(name = "order", defaultValue = "asc") String order,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "paging", defaultValue = "30") int paging) {
    Pageable pageable = PagingUtils.makePageRequestWithCamelCase(sortBy, order, page, paging);
    return ResponseEntity.ok(
        conversationService.getMyConversations(pageable, userPrincipal.getId()));
  }

  @GetMapping("/{conversationId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> getMyConversations(
      @PathVariable UUID conversationId, @CurrentUser UserPrincipal userPrincipal) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            conversationService.getDetailConversation(userPrincipal.getId(), conversationId)));
  }
}
