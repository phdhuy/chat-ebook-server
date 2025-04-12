package com.chatebook.security.service;

import com.chatebook.security.model.OauthToken;
import java.util.UUID;

public interface OauthTokenService {

  OauthToken createToken(UUID userId, UUID refreshToken);

  OauthToken getOauthTokenById(UUID id);

  OauthToken getOauthTokenByRefreshToken(UUID refreshTokenId);

  void revoke(UUID oauthTokenId);

  void revokeAll(OauthToken oauthToken, String email);
}
