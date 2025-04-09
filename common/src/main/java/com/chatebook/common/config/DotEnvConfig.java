package com.chatebook.common.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotEnvConfig {

  @Bean
  public Dotenv dotenv() {
    return Dotenv.configure().filename(".env").load();
  }
}
