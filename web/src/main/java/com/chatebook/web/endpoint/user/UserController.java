package com.chatebook.web.endpoint.user;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.security.annotation.CurrentUser;
import com.chatebook.security.model.UserPrincipal;
import com.chatebook.security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User APIs")
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> getMyProfile(@CurrentUser UserPrincipal userPrincipal) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(userService.getMyInfo(userPrincipal.getId())));
  }
}
