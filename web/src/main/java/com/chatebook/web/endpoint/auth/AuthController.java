package com.chatebook.web.endpoint.auth;

import com.chatebook.common.constant.MessageConstant;
import com.chatebook.common.exception.BadRequestException;
import com.chatebook.common.exception.UnauthorizedException;
import com.chatebook.common.model.enums.Role;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.security.model.UserPrincipal;
import com.chatebook.security.payload.request.RefreshTokenRequest;
import com.chatebook.security.payload.request.SignInRequest;
import com.chatebook.security.payload.request.SignInWithGoogleRequest;
import com.chatebook.security.payload.request.SignUpRequest;
import com.chatebook.security.service.Oauth2Service;
import com.chatebook.security.service.UserService;
import com.chatebook.security.token.TokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth APIs")
public class AuthController {

  private final TokenProvider tokenProvider;

  private final UserService userService;

  private final AuthenticationManager authenticationManager;

  private final Oauth2Service oauth2Service;

  @PostMapping("/sign-in")
  public ResponseDataAPI signIn(@RequestBody @Valid SignInRequest signInRequest) {
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

      return ResponseDataAPI.successWithoutMeta(tokenProvider.createOauthToken(userPrincipal));

    } catch (BadCredentialsException ex) {
      throw new BadRequestException(MessageConstant.INCORRECT_EMAIL_OR_PASSWORD);
    }
  }

  @PostMapping("/google")
  public ResponseDataAPI signInWithGoogle(
      @RequestBody @Valid SignInWithGoogleRequest signInWithGoogleRequest) {
    UserPrincipal userPrincipal =
        UserPrincipal.create(
            oauth2Service.signInWithGoogle(signInWithGoogleRequest.getAccessToken()));

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            userPrincipal, null, userPrincipal.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return ResponseDataAPI.successWithoutMeta(tokenProvider.createOauthToken(userPrincipal));
  }

  @PostMapping("/sign-up")
  public ResponseDataAPI signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
    return ResponseDataAPI.successWithoutMeta(userService.signUp(signUpRequest));
  }

  @PostMapping("/refresh-token")
  public ResponseDataAPI refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
    return ResponseDataAPI.successWithoutMeta(
        tokenProvider.refreshTokenOauthToken(refreshTokenRequest.getRefreshToken(), false));
  }

  @PostMapping("/revoke-token")
  public ResponseDataAPI revokeToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
    tokenProvider.revokeToken(refreshTokenRequest.getRefreshToken());
    return ResponseDataAPI.successWithoutMetaAndData();
  }
}
