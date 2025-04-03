package com.chatebook.web.endpoint.auth;

import com.chatebook.common.constant.MessageConstant;
import com.chatebook.common.exception.BadRequestException;
import com.chatebook.common.exception.UnauthorizedException;
import com.chatebook.common.model.enums.Role;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.security.model.UserPrincipal;
import com.chatebook.security.payload.request.RefreshTokenRequest;
import com.chatebook.security.payload.request.SignInRequest;
import com.chatebook.security.token.TokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Alert APIs")
public class AuthController {

  private final TokenProvider tokenProvider;

  private final AuthenticationManager authenticationManager;

  @PostMapping("/auth/sign-in")
  public ResponseEntity<ResponseDataAPI> signIn(@RequestBody @Valid SignInRequest signInRequest) {
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  signInRequest.getEmail().toLowerCase(), signInRequest.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
      if (userPrincipal.getRole().equals(Role.ROLE_ADMIN)) {
        throw new UnauthorizedException(MessageConstant.UNAUTHORIZED);
      }

      return ResponseEntity.ok(
          ResponseDataAPI.successWithoutMeta(tokenProvider.createOauthToken(userPrincipal)));

    } catch (BadCredentialsException ex) {
      throw new BadRequestException(MessageConstant.INCORRECT_EMAIL_OR_PASSWORD);
    }
  }

  @PostMapping("/auth/refresh-token")
  public ResponseEntity<ResponseDataAPI> refreshToken(
      @RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            tokenProvider.refreshTokenOauthToken(refreshTokenRequest.getRefreshToken(), false)));
  }
}
