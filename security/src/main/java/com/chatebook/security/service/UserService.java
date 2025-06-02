package com.chatebook.security.service;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.security.model.User;
import com.chatebook.security.payload.request.SignUpRequest;
import com.chatebook.security.payload.response.UserInfoResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

  UserInfoResponse signUp(SignUpRequest signUpRequest);

  UserInfoResponse getMyInfo(UUID userId);

  ResponseDataAPI getListUserByAdmin(Pageable pageable);

  User createUserWithGoogle(String email, String avatarUrl, String username);

  User findUserById(UUID userId);

  User findUserByEmail(String email);
}
