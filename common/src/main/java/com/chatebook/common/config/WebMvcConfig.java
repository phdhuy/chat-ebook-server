package com.chatebook.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private static final Long MAX_AGE_SECS = 60L * 60L * 24L * 2L;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedOrigin("*");
    config.addAllowedMethod("*");
    config.addAllowedHeader("*");
    config.setMaxAge(MAX_AGE_SECS);
    source.registerCorsConfiguration("/**", config);
    CorsFilter corsFilter = new CorsFilter(source);

    FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>(corsFilter);
    registration.setOrder(Ordered.HIGHEST_PRECEDENCE);

    return registration;
  }
}
