package com.chatebook.common.constant;

public final class CommonConstant {

  private CommonConstant() {}

  public static final String SUCCESS = "success";

  public static final String FAILURE = "failure";

  public static final String ROLE_PREFIX = "ROLE_";

  public static final String RULE_ROLE = "ADMIN|TEACHER|STUDENT|PARENT";

  public static final String RULE_PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,32})";

  public static final String AUTHORIZATION = "Authorization";

  public static final String BEARER = "Bearer ";

  public static final String ASC = "asc";

  public static final String DESC = "desc";
}
