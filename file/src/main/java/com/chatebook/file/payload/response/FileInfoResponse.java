package com.chatebook.file.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileInfoResponse {

  private UUID id;

  private Timestamp createdAt;

  private String secureUrl;

  private String fileName;

  private String publicId;

  private String format;

  private Integer pages;

  private BigInteger bytes;
}
