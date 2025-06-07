package com.chatebook.security.token;

import com.chatebook.common.constant.MessageConstant;
import com.chatebook.common.exception.ForbiddenException;
import com.chatebook.security.config.TokenProperties;
import com.chatebook.security.mapper.OauthTokenMapper;
import com.chatebook.security.model.OauthToken;
import com.chatebook.security.model.User;
import com.chatebook.security.model.UserPrincipal;
import com.chatebook.security.payload.response.OauthTokenResponse;
import com.chatebook.security.service.OauthTokenService;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

  private final TokenProperties tokenProperties;

  private final OauthTokenService oauthTokenService;

  private final OauthTokenMapper oauthTokenMapper;

  private String createAccessToken(OauthToken oauthToken) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + tokenProperties.getTokenExpirationMsec());

    return Jwts.builder()
        .setSubject(oauthToken.getId().toString())
        .setIssuedAt(new Date())
        .claim("role", oauthToken.getUser().getRole().toString())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, tokenProperties.getTokenSecret())
        .compact();
  }

  private String createRefreshToken(OauthToken oauthToken) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + tokenProperties.getRefreshTokenExpirationMsec());

    return Jwts.builder()
        .setSubject(oauthToken.getRefreshToken().toString())
        .setIssuedAt(new Date())
        .claim("role", oauthToken.getUser().getRole().toString())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, tokenProperties.getRefreshTokenSecret())
        .compact();
  }

  private void validateAccessToken(String authToken, String secret) {
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
    } catch (ExpiredJwtException ex) {
      throw new ForbiddenException(MessageConstant.EXPIRED_TOKEN);
    } catch (Exception ex) {
      throw new ForbiddenException(MessageConstant.INVALID_TOKEN);
    }
  }

  private void validateRefreshToken(String authToken, String secret) {
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
    } catch (ExpiredJwtException ex) {
      throw new ForbiddenException(MessageConstant.EXPIRED_REFRESH_TOKEN);
    } catch (Exception ex) {
      throw new ForbiddenException(MessageConstant.INVALID_REFRESH_TOKEN);
    }
  }

  public User getUserFromToken(String token) {
    return this.getOauthTokenByToken(token).getUser();
  }

  public OauthToken getOauthTokenByToken(String token) {
    this.validateAccessToken(token, tokenProperties.getTokenSecret());
    OauthToken oauthToken =
        oauthTokenService.getOauthTokenById(
            this.getUUIDFromToken(token, tokenProperties.getTokenSecret()));
    if (oauthToken.getRevokedAt() != null) {
      throw new ForbiddenException(MessageConstant.REVOKED_TOKEN);
    }
    return oauthToken;
  }

  private UUID getUUIDFromToken(String token, String secret) {
    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    return UUID.fromString(claims.getSubject());
  }

  public OauthTokenResponse createToken(UUID userId) {
    OauthToken oauthToken = oauthTokenService.createToken(userId, null);
    return oauthTokenMapper.toOauthTokenResponse(
        this.createAccessToken(oauthToken),
        this.createRefreshToken(oauthToken),
        tokenProperties.getTokenExpirationMsec() / 1000,
        oauthToken.getCreatedAt());
  }

  public OauthTokenResponse createOauthToken(UserPrincipal userPrincipal) {
    return this.createToken(userPrincipal.getId());
  }

  public OauthTokenResponse refreshTokenOauthToken(String refreshToken) {
    this.validateRefreshToken(refreshToken, tokenProperties.getRefreshTokenSecret());
    UUID refreshTokenId =
        this.getUUIDFromToken(refreshToken, tokenProperties.getRefreshTokenSecret());

    OauthToken oauthToken = oauthTokenService.getOauthTokenByRefreshToken(refreshTokenId);

    OauthToken result = oauthTokenService.createToken(oauthToken.getUser().getId(), refreshTokenId);
    return oauthTokenMapper.toOauthTokenResponse(
        this.createAccessToken(result),
        refreshToken,
        tokenProperties.getTokenExpirationMsec() / 1000,
        result.getCreatedAt());
  }

  public void revokeToken(String refreshToken) {
    this.validateRefreshToken(refreshToken, tokenProperties.getRefreshTokenSecret());
    UUID refreshTokenId =
        this.getUUIDFromToken(refreshToken, tokenProperties.getRefreshTokenSecret());
    oauthTokenService.revoke(refreshTokenId);
  }
}
