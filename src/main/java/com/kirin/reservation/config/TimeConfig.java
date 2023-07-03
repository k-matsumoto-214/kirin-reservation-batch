package com.kirin.reservation.config;

import com.kirin.reservation.model.ReservationTime;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

@Configuration
@Slf4j
public class TimeConfig {

  @Value("${kirin.start-time.am}")
  @DateTimeFormat(pattern = "HH:mm:ss")
  private LocalTime startTimeAm;

  @Value("${kirin.start-time.pm}")
  @DateTimeFormat(pattern = "HH:mm:ss")
  private LocalTime startTimePm;


  /**
   * 現在時刻を取得する
   *
   * @param clock
   * @return 現在時刻
   */
  public LocalDateTime getNow(Clock clock) {
    return LocalDateTime.now(clock);
  }

  /**
   * 現在曜日を取得する
   *
   * @return 現在曜日
   */
  public DayOfWeek getNowDayOfWeek(Clock clock) {
    return LocalDateTime.now(clock).getDayOfWeek();
  }

  /**
   * 渡された時刻からその時刻が午前,午後予約か判断して時間帯ドメインを返す
   *
   * @param clock
   * @return 予約時間帯ドメイン
   */
  public ReservationTime getReservationTime(Clock clock) {
    if (LocalDateTime.now(clock)
        .isBefore(
            LocalDateTime.now(clock).with(startTimeAm))) {
      return ReservationTime.AM;
    } else {
      return ReservationTime.PM;
    }
  }

  /**
   * 引数の予約時間帯タイプに対応する予約開始時間を取得する
   *
   * @param reservationTime 予約時間帯オブジェクト
   * @param clock
   * @return 予約時間帯タイプに対応する予約開始時間
   */
  public LocalDateTime getTargetTime(ReservationTime reservationTime, Clock clock) {

    if (Objects.equals(reservationTime, ReservationTime.AM)) {
      // 午前予約の時
      return LocalDateTime.now(clock).with(startTimeAm);

    } else {
      //　午後予約の時
      return LocalDateTime.now(clock).with(startTimePm);
    }

  }

  /**
   * 引数の待機目標時刻になるまで待機する
   *
   * @param target 待機目標の時刻
   * @param clock
   */
  public void until(LocalDateTime target, Clock clock) {
    int waitCount = 0;

    while (LocalDateTime.now(clock).isBefore(target)) {
      // 100,000,000回ループするごとにログ表示
      if (waitCount % (1000 * 1000 * 100) == 0) {
        log.info("待機中");
      }
      waitCount++;
    }
  }
}
