package com.kirin.reservation.config;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

@Configuration
public class StartDateTimeConfig {

  @Value("${kirin.start-time}")
  @DateTimeFormat(pattern = "HH:mm:ss")
  private LocalTime startTime;

  /**
   * 日付を当日、時刻を環境変数で指定したLocalDateTimeを取得する
   * 
   * @return 予約開始時間のLocalDateTime
   */
  public LocalDateTime getStartDateTime() {
    return LocalDateTime.now(ZoneId.of("Asia/Tokyo")).with(startTime);
  }
}
