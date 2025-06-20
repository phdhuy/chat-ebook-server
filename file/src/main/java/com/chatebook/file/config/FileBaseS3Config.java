package com.chatebook.file.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileBaseS3Config {

  @Value("${filebase.s3.access-key}")
  private String accessKey;

  @Value("${filebase.s3.secret-key}")
  private String secretKey;

  @Value("${filebase.s3.endpoint}")
  private String endpoint;

  @Value("${filebase.s3.region}")
  private String region;

  @Bean
  public AmazonS3 filebaseS3Client() {
    BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
    return AmazonS3ClientBuilder.standard()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
        .withPathStyleAccessEnabled(true)
        .withCredentials(new AWSStaticCredentialsProvider(creds))
        .build();
  }
}
