package com.chatebook.security.service;

import com.chatebook.security.model.User;
import com.chatebook.security.payload.request.SignUpRequest;
import com.chatebook.security.payload.response.UserInfoResponse;
import java.util.UUID;

public interface UserService {

  UserInfoResponse signUp(SignUpRequest signUpRequest);

  UserInfoResponse getMyInfo(UUID userId);

  User createUserWithGoogle(String email);

  User findUserById(UUID userId);

  User findUserByEmail(String email);
}
