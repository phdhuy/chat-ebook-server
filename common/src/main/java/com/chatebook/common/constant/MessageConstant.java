package com.chatebook.common.constant;

public final class MessageConstant {

  private MessageConstant() {}

  // Common
  public static final String UNAUTHORIZED = "unauthorized";
  public static final String FORBIDDEN_ERROR = "forbidden_error";
  public static final String INTERNAL_SERVER_ERROR = "internal_server_error";
  public static final String UPLOAD_FILE_ERROR = "upload_file_error";
  public static final String AI_SERVICE_ERROR = "ai_service_error";

  // Auth
  public static final String INCORRECT_EMAIL_OR_PASSWORD = "incorrect_email_or_password";

  // Oauth token
  public static final String OAUTH_TOKEN_NOT_FOUND = "oauth_token_not_found";

  public static final String INVALID_REFRESH_TOKEN = "invalid_refresh_token";

  public static final String EXPIRED_TOKEN = "expired_token";

  public static final String INVALID_TOKEN = "invalid_token";

  public static final String EXPIRED_REFRESH_TOKEN = "expired_refresh_token";

  public static final String REVOKED_TOKEN = "revoked_token";

  public static final String INVALID_ACCESS_TOKEN_GOOGLE = "invalid_access_token_google";

  // User
  public static final String USER_NOT_FOUND = "user_not_found";

  public static final String ACCOUNT_NOT_EXISTS = "account_not_exists";

  public static final String ACCOUNT_BLOCKED = "account_blocked";

  public static final String ACCOUNT_NOT_ACTIVATED = "account_not_activated";

  // Sign up
  public static final String PASSWORD_AND_CONFIRMATION_PASSWORD_IS_NOT_MATCH =
      "password_and_confirmation_password_is_not_match";

  public static final String EMAIL_ALREADY_IN_USE = "email_already_in_use";

  // Conversation
  public static final String CONVERSATION_NOT_FOUND = "conversation_not_found";

  // MindMap
  public static final String MINDMAP_NOT_FOUND = "mindmap_not_found";

  // Usage
  public static final String FROM_DATE_MUST_BE_BEFORE_TO_DATE = "from_date_must_be_before_to_date";
}
