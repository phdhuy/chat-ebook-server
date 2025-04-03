package com.chatebook.file.config;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CloudinaryConfig {

  private final CloudinaryProperties cloudinaryProperties;

  @Bean
  public Cloudinary cloudinary() {
    Cloudinary cloudinary;
    Map<String, String> config = new HashMap<>();
    config.put("cloud_name", cloudinaryProperties.getCloudName());
    config.put("api_key", cloudinaryProperties.getApiKey());
    config.put("api_secret", cloudinaryProperties.getApiSecret());
    cloudinary = new Cloudinary(config);
    return cloudinary;
  }
}
