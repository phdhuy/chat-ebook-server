package com.chatebook.file.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CloudinaryProperties {

  private final Dotenv dotenv;

  public String getCloudName() {
    return dotenv.get("CLOUDINARY_CLOUD_NAME");
  }

  public String getApiKey() {
    return dotenv.get("CLOUDINARY_API_KEY");
  }

  public String getApiSecret() {
    return dotenv.get("CLOUDINARY_API_SECRET");
  }
}
