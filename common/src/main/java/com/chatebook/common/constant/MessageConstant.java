package com.chatebook.common.constant;

public final class MessageConstant {

  private MessageConstant() {}

  // Common
  public static final String UNAUTHORIZED = "unauthorized";
  public static final String FORBIDDEN_ERROR = "forbidden_error";
  public static final String INTERNAL_SERVER_ERROR = "internal_server_error";

  // Auth
  public static final String INCORRECT_EMAIL_OR_PASSWORD = "incorrect_email_or_password";

  // Oauth token
  public static final String OAUTH_TOKEN_NOT_FOUND = "oauth_token_not_found";

  public static final String INVALID_REFRESH_TOKEN = "invalid_refresh_token";

  public static final String EXPIRED_TOKEN = "expired_token";

  public static final String INVALID_TOKEN = "invalid_token";

  public static final String EXPIRED_REFRESH_TOKEN = "expired_refresh_token";

  public static final String REVOKED_TOKEN = "revoked_token";

  // User
  public static final String USER_NOT_FOUND = "user_not_found";

  public static final String ACCOUNT_NOT_EXISTS = "account_not_exists";

  public static final String ACCOUNT_BLOCKED = "account_blocked";

  public static final String ACCOUNT_NOT_ACTIVATED = "account_not_activated";

  // Sign up
  public static final String PASSWORD_AND_CONFIRMATION_PASSWORD_IS_NOT_MATCH =
      "password_and_confirmation_password_is_not_match";

  public static final String EMAIL_ALREADY_IN_USE = "email_already_in_use";

  // User info
  public static final String USER_INFO_ALREADY_EXISTS = "user_info_already_exists";

  public static final String USER_INFO_NOT_FOUND = "user_info_not_found";

  // Teacher profile

  public static final String TEACHER_PROFILE_NOT_FOUND = "teacher_profile_not_found";

  // Parent profile
  public static final String PARENT_PROFILE_NOT_FOUND = "parent_profile_not_found";
}
