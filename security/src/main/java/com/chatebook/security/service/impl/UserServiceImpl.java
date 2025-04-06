package com.chatebook.security.service.impl;

import com.chatebook.common.common.CommonFunction;
import com.chatebook.common.constant.MessageConstant;
import com.chatebook.common.exception.BadRequestException;
import com.chatebook.common.exception.NotFoundException;
import com.chatebook.common.model.enums.Role;
import com.chatebook.security.mapper.UserMapper;
import com.chatebook.security.model.User;
import com.chatebook.security.payload.request.SignUpRequest;
import com.chatebook.security.payload.response.UserInfoResponse;
import com.chatebook.security.repository.UserRepository;
import com.chatebook.security.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  @Override
  public UserInfoResponse signUp(SignUpRequest signUpRequest) {
    this.checkValidSignUp(
        signUpRequest.getPassword(),
        signUpRequest.getConfirmationPassword(),
        signUpRequest.getEmail());

    User user = new User();
    user.setEmail(signUpRequest.getEmail().toLowerCase());
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    user.setIsConfirmed(true);
    user.setConfirmedAt(CommonFunction.getCurrentDateTime());
    user.setRole(Role.ROLE_USER);

    return userMapper.toUserInfoResponse(userRepository.save(user));
  }

  @Override
  public UserInfoResponse getMyInfo(UUID userId) {
    User user = this.findUserById(userId);
    return userMapper.toUserInfoResponse(user);
  }

  @Override
  public User createUserWithGoogle(String email) {
    User user = new User();

    user.setEmail(email);
    user.setIsConfirmed(true);
    user.setConfirmedAt(CommonFunction.getCurrentDateTime());
    user.setRole(Role.ROLE_USER);

    return userRepository.save(user);
  }

  private void checkValidSignUp(String password, String passwordConfirmation, String email) {
    if (userRepository.existsByEmail(email.toLowerCase())) {
      throw new BadRequestException(MessageConstant.EMAIL_ALREADY_IN_USE);
    }
    if (!password.equals(passwordConfirmation)) {
      throw new BadRequestException(
          MessageConstant.PASSWORD_AND_CONFIRMATION_PASSWORD_IS_NOT_MATCH);
    }
  }

  @Override
  public User findUserById(UUID userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
  }

  @Override
  public User findUserByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
  }
}
