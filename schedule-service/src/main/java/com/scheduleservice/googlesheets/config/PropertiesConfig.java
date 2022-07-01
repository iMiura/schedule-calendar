package com.scheduleservice.googlesheets.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class PropertiesConfig {

  private static final String[] BASE_NAMES = {"classpath:/messages"};

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource resourceBundleMessageSource
        = new ReloadableResourceBundleMessageSource();
    resourceBundleMessageSource.setBasenames(BASE_NAMES);
    return resourceBundleMessageSource;

  }
}
