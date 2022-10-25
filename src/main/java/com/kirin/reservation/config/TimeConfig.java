package com.kirin.reservation.config;

import com.kirin.reservation.model.ReservationTime;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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

  /**
   * 渡された時刻からその時刻が午前,午後予約か判断して時間帯ドメインを返す
   *
   * @param now 現在時刻
   * @return 予約時間帯ドメイン
   */
  public ReservationTime getReservationTime(LocalDateTime now) {
    if (now.isBefore(this.getStartDateTimeAm())) {
      return ReservationTime.AM;
    } else {
      return ReservationTime.PM;
    }
  }

  /**
   * 引数の予約開始時間帯になるまで待機する
   *
   * @param reservationTime 待機目標の予約時間帯オブジェクト
   */
  public void until(ReservationTime reservationTime) {
    LocalDateTime target;

    if (Objects.equals(reservationTime, ReservationTime.AM)) {
      // 午前予約の時
      target = this.getStartDateTimeAm();
    } else {
      //　午後予約の時
      target = this.getStartDateTimePm();
    }

    int waitCount = 0;

    while (this.getNow().isBefore(target)) {
      // 100,000,000回ループするごとにログ表示
      if (waitCount % (1000 * 1000 * 100) == 0) {
        log.info("待機中");
      }
      waitCount++;
    }
  }
}
