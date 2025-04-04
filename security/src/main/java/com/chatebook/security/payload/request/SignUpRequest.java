package com.chatebook.security.payload.request;

import com.chatebook.common.constant.CommonConstant;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpRequest {

  @NotBlank @Email private String email;

  @NotBlank
  @Pattern(regexp = CommonConstant.RULE_PASSWORD)
  private String password;

  @NotBlank
  @Pattern(regexp = CommonConstant.RULE_PASSWORD)
  private String confirmationPassword;
}
