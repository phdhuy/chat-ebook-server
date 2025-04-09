package com.chatebook.security.mapper;

import com.chatebook.common.config.SpringMapStructConfig;
import com.chatebook.security.model.User;
import com.chatebook.security.payload.response.UserInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SpringMapStructConfig.class)
public interface UserMapper {

  @Mapping(source = "user.id", target = "id")
  @Mapping(source = "user.email", target = "email")
  @Mapping(source = "user.confirmedAt", target = "confirmedAt")
  @Mapping(source = "user.createdAt", target = "createdAt")
  @Mapping(source = "user.isConfirmed", target = "isConfirmed")
  @Mapping(source = "user.username", target = "username")
  @Mapping(source = "user.avatarUrl", target = "avatarUrl")
  UserInfoResponse toUserInfoResponse(User user);
}
