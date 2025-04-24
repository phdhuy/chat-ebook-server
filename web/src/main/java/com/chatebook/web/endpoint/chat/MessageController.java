package com.chatebook.web.endpoint.chat;

import com.chatebook.chat.payload.request.CreateMessageRequest;
import com.chatebook.chat.service.MessageService;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.common.utils.PagingUtils;
import com.chatebook.security.annotation.CurrentUser;
import com.chatebook.security.model.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversations")
@Tag(name = "Message APIs")
public class MessageController {

  private final MessageService messageService;

  @PostMapping("/{conversationId}/messages")
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI createMessage(
      @CurrentUser UserPrincipal userPrincipal,
      @PathVariable UUID conversationId,
      @RequestBody @Valid CreateMessageRequest createMessageRequest) {
    return ResponseDataAPI.successWithoutMeta(
        messageService.sendMessage(userPrincipal.getId(), conversationId, createMessageRequest));
  }

  @GetMapping("/{conversationId}/messages")
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI getListMessageByConversationId(
      @CurrentUser UserPrincipal userPrincipal,
      @PathVariable UUID conversationId,
      @RequestParam(name = "sort", defaultValue = "created_at") String sortBy,
      @RequestParam(name = "order", defaultValue = "asc") String order,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "paging", defaultValue = "30") int paging) {
    Pageable pageable = PagingUtils.makePageRequestWithCamelCase(sortBy, order, page, paging);
    return messageService.getListMessageByConversationId(
        pageable, userPrincipal.getId(), conversationId);
  }
}
