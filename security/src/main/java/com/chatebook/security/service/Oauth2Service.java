package com.chatebook.security.service;

import com.chatebook.security.model.User;

public interface Oauth2Service {

  User signInWithGoogle(String accessToken);
}
