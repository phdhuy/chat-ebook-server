package com.chatebook.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQAdapter {

  private final RabbitTemplate rabbitTemplate;

  @Async("asyncExecutor")
  public void pushMessageToQueue(String exchangeName, String routingKey, Object message) {
    rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
  }
}