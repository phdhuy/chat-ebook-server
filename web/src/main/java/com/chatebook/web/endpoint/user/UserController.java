package com.chatebook.web.endpoint.user;

import com.chatebook.common.common.CommonFunction;
import com.chatebook.common.config.RabbitMQConfig;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.common.payload.response.QueueNameResponse;
import com.chatebook.security.annotation.CurrentUser;
import com.chatebook.security.model.UserPrincipal;
import com.chatebook.security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User APIs")
public class UserController {

  private final UserService userService;

  private final AmqpAdmin amqpAdmin;

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> getMyProfile(@CurrentUser UserPrincipal userPrincipal) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(userService.getMyInfo(userPrincipal.getId())));
  }

  @PostMapping("/queues")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> createQueue(@CurrentUser UserPrincipal userPrincipal) {
    String queueName = CommonFunction.generateQueueName(userPrincipal.getId());
    Queue queue = new Queue(queueName, true, false, true);
    amqpAdmin.declareQueue(queue);

    Binding binding =
        BindingBuilder.bind(queue)
            .to(new DirectExchange(RabbitMQConfig.EXCHANGE_NAME))
            .with(queueName);

    amqpAdmin.declareBinding(binding);

    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            QueueNameResponse.builder().queueName(queueName).build()));
  }
}
