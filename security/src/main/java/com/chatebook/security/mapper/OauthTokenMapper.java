package com.chatebook.security.mapper;

import com.chatebook.common.config.SpringMapStructConfig;
import com.chatebook.security.payload.response.OauthTokenResponse;
import java.sql.Timestamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface OauthTokenMapper {

  @Mapping(source = "accessToken", target = "accessToken")
  @Mapping(source = "refreshToken", target = "refreshToken")
  @Mapping(source = "expiresIn", target = "expiresIn")
  @Mapping(source = "createdAt", target = "createdAt")
  OauthTokenResponse toOauthTokenResponse(
      String accessToken, String refreshToken, long expiresIn, Timestamp createdAt);
}
