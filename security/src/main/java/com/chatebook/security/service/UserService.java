package com.chatebook.security.service;

import com.chatebook.security.model.User;

import java.util.UUID;

public interface UserService {

  User createUser(String email, String password, String passwordConfirmation, String role);

  User findUserById(UUID userId);

  User findUserByEmail(String email);
}
