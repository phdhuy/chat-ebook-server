package com.chatebook.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.chatebook.*"})
@EnableJpaRepositories(basePackages = {"com.chatebook.*"})
@ComponentScan(basePackages = {"com.chatebook.*"})
public class ChatEbookApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChatEbookApplication.class, args);
  }
}
