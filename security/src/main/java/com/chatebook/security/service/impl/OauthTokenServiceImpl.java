package com.chatebook.security.service.impl;

import com.chatebook.common.common.CommonFunction;
import com.chatebook.common.constant.MessageConstant;
import com.chatebook.common.exception.ForbiddenException;
import com.chatebook.common.exception.NotFoundException;
import com.chatebook.security.model.OauthToken;
import com.chatebook.security.repository.OauthTokenRepository;
import com.chatebook.security.service.OauthTokenService;
import com.chatebook.security.service.UserService;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthTokenServiceImpl implements OauthTokenService {

  private final OauthTokenRepository oauthTokenRepository;

  private final UserService userService;

  @Override
  public OauthToken createToken(UUID userId, UUID refreshToken) {
    try {
      OauthToken oauthToken = new OauthToken();
      oauthToken.setUser(userService.findUserById(userId));
      oauthToken.setRefreshToken(Objects.requireNonNullElseGet(refreshToken, UUID::randomUUID));
      return oauthTokenRepository.save(oauthToken);
    } catch (Exception e) {
      throw new ForbiddenException(MessageConstant.INVALID_REFRESH_TOKEN);
    }
  }

  @Override
  public OauthToken getOauthTokenById(UUID id) {
    return oauthTokenRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MessageConstant.OAUTH_TOKEN_NOT_FOUND));
  }

  @Override
  public OauthToken getOauthTokenByRefreshToken(UUID refreshTokenId) {
    OauthToken oauthToken =
        oauthTokenRepository
            .findByRefreshToken(refreshTokenId)
            .orElseThrow(() -> new NotFoundException(MessageConstant.OAUTH_TOKEN_NOT_FOUND));
    if (oauthToken.getRevokedAt() != null) {
      throw new ForbiddenException(MessageConstant.REVOKED_TOKEN);
    }
    oauthToken.setRevokedAt(CommonFunction.getCurrentDateTime());
    oauthToken.setRefreshToken(null);
    return oauthTokenRepository.save(oauthToken);
  }

  @Override
  public void revoke(OauthToken oauthToken) {}

  @Override
  public void revokeAll(OauthToken oauthToken, String email) {}
}
