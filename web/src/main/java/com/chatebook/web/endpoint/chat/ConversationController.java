package com.chatebook.web.endpoint.chat;

import com.chatebook.chat.service.ConversationService;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.common.utils.PagingUtils;
import com.chatebook.security.annotation.CurrentUser;
import com.chatebook.security.model.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversations")
@Tag(name = "Conversation APIs")
public class ConversationController {

  private final ConversationService conversationService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('USER')")
  @Operation(
      summary = "Create Conversation",
      description = "Creates a conversation using an uploaded file.")
  public ResponseDataAPI createConversation(
      @CurrentUser UserPrincipal userPrincipal,
      @Parameter(description = "File to upload", required = true) @RequestPart("file")
          MultipartFile file) {
    return ResponseDataAPI.successWithoutMeta(
        conversationService.createConversation(userPrincipal.getId(), file));
  }

  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI getMyConversations(
      @CurrentUser UserPrincipal userPrincipal,
      @RequestParam(name = "sort", defaultValue = "created_at") String sortBy,
      @RequestParam(name = "order", defaultValue = "asc") String order,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "paging", defaultValue = "30") int paging) {
    Pageable pageable = PagingUtils.makePageRequestWithCamelCase(sortBy, order, page, paging);
    return conversationService.getMyConversations(pageable, userPrincipal.getId());
  }

  @GetMapping("/{conversationId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI getDetailConversation(
      @PathVariable UUID conversationId, @CurrentUser UserPrincipal userPrincipal) {
    return ResponseDataAPI.successWithoutMeta(
        conversationService.getDetailConversation(userPrincipal.getId(), conversationId));
  }
}
