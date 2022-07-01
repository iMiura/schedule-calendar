package com.scheduleservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@SpringBootTest
class ScheduleServiceApplicationTests {

  @Test
  void contextLoads() {
  }

  public static void main(String[] args) {
    LocalDate date = LocalDate.of(2022, 6, 26);
    LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth()); // 获取当前月的最后一天
    System.out.println(date);
    System.out.println(date.getDayOfWeek().getValue());
    System.out.println(lastDay);
  }



}
