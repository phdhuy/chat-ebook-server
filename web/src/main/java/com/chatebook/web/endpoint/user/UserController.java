package com.chatebook.web.endpoint.user;

import com.chatebook.common.common.CommonFunction;
import com.chatebook.common.config.RabbitMQConfig;
import com.chatebook.common.constant.CommonConstant;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.common.payload.response.QueueNameResponse;
import com.chatebook.security.annotation.CurrentUser;
import com.chatebook.security.model.UserPrincipal;
import com.chatebook.security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User APIs")
public class UserController {

  private final UserService userService;

  private final AmqpAdmin amqpAdmin;

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI getMyProfile(@CurrentUser UserPrincipal userPrincipal) {
    return ResponseDataAPI.successWithoutMeta(userService.getMyInfo(userPrincipal.getId()));
  }

  @PostMapping("/queues")
  @PreAuthorize("hasRole('USER')")
  public ResponseDataAPI createQueue(@CurrentUser UserPrincipal userPrincipal) {
    String queueName = CommonFunction.generateQueueName(userPrincipal.getId());
    Map<String, Object> args = new HashMap<>();
    args.put("x-expires", CommonConstant.RABBITMQ_TTL);
    Queue queue = new Queue(queueName, true, false, false, args);
    amqpAdmin.declareQueue(queue);

    Binding binding =
        BindingBuilder.bind(queue)
            .to(new DirectExchange(RabbitMQConfig.DIRECT_EXCHANGE_NAME))
            .with(userPrincipal.getId().toString());

    amqpAdmin.declareBinding(binding);

    return ResponseDataAPI.successWithoutMeta(
        QueueNameResponse.builder().queueName(queueName).build());
  }
}
