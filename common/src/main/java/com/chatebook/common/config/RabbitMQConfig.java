package com.chatebook.common.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  public static final String EXCHANGE_NAME = "user.direct.exchange";

  @Bean
  public DirectExchange directExchange() {
    return new DirectExchange(EXCHANGE_NAME);
  }
}
