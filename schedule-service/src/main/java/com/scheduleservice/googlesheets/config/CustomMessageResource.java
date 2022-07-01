package com.scheduleservice.googlesheets.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CustomMessageResource {

  @Autowired
  private MessageSource messageSource;

  public String getMessage(String code) {
    return this.messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
  }

  public String getMessage(String code, String str) {
    return this.messageSource.getMessage(code, new Object[]{str}, LocaleContextHolder.getLocale());
  }

  public String getMessage(String code, Object[] obj) {
    return this.messageSource.getMessage(code, obj, LocaleContextHolder.getLocale());
  }

}
