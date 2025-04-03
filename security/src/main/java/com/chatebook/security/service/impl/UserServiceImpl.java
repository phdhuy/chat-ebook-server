package com.chatebook.security.service.impl;

import com.chatebook.common.constant.CommonConstant;
import com.chatebook.common.constant.MessageConstant;
import com.chatebook.common.exception.BadRequestException;
import com.chatebook.common.exception.NotFoundException;
import com.chatebook.common.model.enums.Role;
import com.chatebook.security.model.User;
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

  @Override
  public User createUser(String email, String password, String passwordConfirmation, String role) {
    this.checkValidCreateUser(password, passwordConfirmation, email);

    User user = new User();
    user.setEmail(email.toLowerCase());
    user.setPassword(passwordEncoder.encode(password));
    user.setRole(Role.valueOf(CommonConstant.ROLE_PREFIX.concat(role)));

    return userRepository.save(user);
  }

  private void checkValidCreateUser(String password, String passwordConfirmation, String email) {
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
