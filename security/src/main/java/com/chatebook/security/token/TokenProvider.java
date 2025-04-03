package com.chatebook.security.token;

import com.chatebook.common.constant.MessageConstant;
import com.chatebook.common.exception.ForbiddenException;
import com.chatebook.common.model.enums.Role;
import com.chatebook.security.config.TokenProperties;
import com.chatebook.security.mapper.OauthTokenMapper;
import com.chatebook.security.model.OauthToken;
import com.chatebook.security.model.User;
import com.chatebook.security.model.UserPrincipal;
import com.chatebook.security.payload.response.OauthTokenResponse;
import com.chatebook.security.service.OauthTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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

  public OauthTokenResponse refreshTokenOauthToken(String refreshToken, boolean isAdmin) {
    this.validateRefreshToken(refreshToken, tokenProperties.getRefreshTokenSecret());
    UUID refreshTokenId =
        this.getUUIDFromToken(refreshToken, tokenProperties.getRefreshTokenSecret());

    OauthToken oauthToken = oauthTokenService.getOauthTokenByRefreshToken(refreshTokenId);
    if (isAdmin && !oauthToken.getUser().getRole().equals(Role.ROLE_ADMIN)
        || !isAdmin && oauthToken.getUser().getRole().equals(Role.ROLE_ADMIN)) {
      throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
    }
    OauthToken result = oauthTokenService.createToken(oauthToken.getUser().getId(), refreshTokenId);
    return oauthTokenMapper.toOauthTokenResponse(
        this.createAccessToken(result),
        refreshToken,
        tokenProperties.getTokenExpirationMsec() / 1000,
        result.getCreatedAt());
  }
}
