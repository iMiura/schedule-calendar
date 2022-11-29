package com.scheduleservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan(value = "com.scheduleservice.googlesheets.repository.mapper")
public class ScheduleServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScheduleServiceApplication.class, args);
  }

}
