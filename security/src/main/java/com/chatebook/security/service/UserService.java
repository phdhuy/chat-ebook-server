package com.chatebook.security.service;

import com.chatebook.security.model.User;
import com.chatebook.security.payload.request.SignUpRequest;
import com.chatebook.security.payload.response.UserInfoResponse;

import java.util.UUID;

public interface UserService {

  UserInfoResponse signUp(SignUpRequest signUpRequest);

  User findUserById(UUID userId);

  User findUserByEmail(String email);
}
