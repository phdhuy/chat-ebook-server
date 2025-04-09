package com.chatebook.security.service.impl;

import com.chatebook.common.constant.CommonConstant;
import com.chatebook.common.constant.MessageConstant;
import com.chatebook.common.exception.BadRequestException;
import com.chatebook.security.model.User;
import com.chatebook.security.payload.response.UserInfoGoogleResponse;
import com.chatebook.security.repository.UserRepository;
import com.chatebook.security.service.Oauth2Service;
import com.chatebook.security.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2ServiceImpl implements Oauth2Service {

  private final UserRepository userRepository;

  private final UserService userService;

  private final OkHttpClient okHttpClient;

  private final ObjectMapper objectMapper;

  @Override
  public User signInWithGoogle(String accessToken) {
    Request googleRequest =
        new Request.Builder().url(CommonConstant.GOOGLE_AUTH_URL + accessToken).build();

    try (Response googleResponse = okHttpClient.newCall(googleRequest).execute()) {
      if (googleResponse.isSuccessful() && googleResponse.body() != null) {
        String responseBody = googleResponse.body().string();
        UserInfoGoogleResponse googleUser =
            objectMapper.readValue(responseBody, UserInfoGoogleResponse.class);

        Optional<User> user = userRepository.findByEmail(googleUser.getEmail());
        return user.orElseGet(
            () ->
                userService.createUserWithGoogle(
                    googleUser.getEmail(), googleUser.getPicture(), googleUser.getName()));
      } else {
        throw new BadRequestException(MessageConstant.INVALID_ACCESS_TOKEN_GOOGLE);
      }
    } catch (IOException e) {
      log.error("Error during Google authentication: {}", e.getMessage());
      throw new BadRequestException("Error during Google authentication");
    }
  }
}
