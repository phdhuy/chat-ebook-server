package com.chatebook.web.endpoint.health;

import com.chatebook.common.payload.general.ResponseDataAPI;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/healths")
@Tag(name = "Health APIs")
public class HealthController {

  @GetMapping()
  public ResponseEntity<ResponseDataAPI> healthApi() {
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta("OK"));
  }
}
