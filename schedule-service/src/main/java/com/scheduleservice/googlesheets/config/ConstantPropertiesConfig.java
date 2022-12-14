package com.scheduleservice.googlesheets.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:constant.properties")
@Data
@Component
public class ConstantPropertiesConfig {

  @Value("${credentials.file.path}")
  private String credentialsFilePath;

  @Value("${tokens.file.path}")
  private String tokensFilePath;

  @Value("${jwt.secret.key}")
  private String jwtSecretKey;

  @Value("${jwt.normal.expires.at}")
  private String jwtNormalExpiresAt;

  @Value("${jwt.refresh.expires.at}")
  private String jwtRefreshExpiresAt;

  @Value("${jwt.remember.me.expires.at}")
  private String jwtRememberMeExpiresAt;

  @Value("${SLACK_TOKEN}")
  private String slackToken;
}
