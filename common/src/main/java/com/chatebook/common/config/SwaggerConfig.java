package com.chatebook.common.config;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    String schemeName = "bearerAuth";

    return new OpenAPI()
        .components(
            new Components()
                .addSecuritySchemes(
                    schemeName,
                    new SecurityScheme()
                        .name(schemeName)
                        .type(HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .info(apiInfo())
        .addSecurityItem(new SecurityRequirement().addList(schemeName))
        .addServersItem(
            new Server().url("https://marketsentry.site/").description("Production Env"))
        .addServersItem(new Server().url("http://localhost:8081/").description("Dev Env"));
  }

  private Info apiInfo() {
    return new Info().title("ChatEbook API").version("1.0.0");
  }
}
