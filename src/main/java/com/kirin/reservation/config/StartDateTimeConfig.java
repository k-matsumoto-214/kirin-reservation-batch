package com.kirin.reservation.config;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

@Configuration
public class StartDateTimeConfig {

  @Value("${kirin.start-time.am}")
  @DateTimeFormat(pattern = "HH:mm:ss")
  private LocalTime startTimeAm;

  @Value("${kirin.start-time.pm}")
  @DateTimeFormat(pattern = "HH:mm:ss")
  private LocalTime startTimePm;

  private static ZoneId zoneId = ZoneId.of("Asia/Tokyo");

  /**
   * 日付を当日、時刻を環境変数で指定したLocalDateTimeを取得する
   * 
   * @return 午前予約開始時間のLocalDateTime
   */
  public LocalDateTime getStartDateTimeAm() {
    return LocalDateTime.now(zoneId).with(startTimeAm);
  }

  /**
   * 日付を当日、時刻を環境変数で指定したLocalDateTimeを取得する
   * 
   * @return 午後予約開始時間のLocalDateTime
   */
  public LocalDateTime getStartDateTimePm() {
    return LocalDateTime.now(zoneId).with(startTimePm);
  }

  /**
   * 現在時刻を取得する
   * 
   * @return 現在時刻
   */
  public LocalDateTime getNow() {
    return LocalDateTime.now(zoneId);
  }
}
