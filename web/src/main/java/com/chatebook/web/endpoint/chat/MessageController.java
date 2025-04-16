package com.chatebook.web.endpoint.chat;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.security.annotation.CurrentUser;
import com.chatebook.security.model.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversations")
@Tag(name = "Message APIs")
public class MessageController {

  @PostMapping()
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI createMessage(@CurrentUser UserPrincipal userPrincipal) {
    return ResponseDataAPI.successWithoutMetaAndData();
  }
}
