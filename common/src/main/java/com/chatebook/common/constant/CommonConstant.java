package com.chatebook.common.constant;

import okhttp3.MediaType;

public final class CommonConstant {

  private CommonConstant() {}

  public static final String SUCCESS = "success";

  public static final String FAILURE = "failure";

  public static final String ROLE_PREFIX = "ROLE_";

  public static final String RULE_PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,32})";

  public static final String AUTHORIZATION = "Authorization";

  public static final String BEARER = "Bearer ";

  public static final String ASC = "asc";

  public static final String DESC = "desc";

  public static final String GOOGLE_AUTH_URL =
      "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";

  public static final MediaType JSON_MEDIA_TYPE =
      MediaType.parse("application/json; charset=utf-8");

  public static final MediaType PDF_MEDIA_TYPE = MediaType.parse("application/pdf");

  public static final String AI_BASE_URL = "http://rag-ai:5000";

  public static final long RABBITMQ_TTL = 172800000;

  public static final long FILE_SIZE_10MB = 10L * 1024 * 1024;
}
