package com.kirin.reservation.task;

import java.net.MalformedURLException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kirin.reservation.config.StartDateTimeConfig;
import com.kirin.reservation.model.ReservationDate;
import com.kirin.reservation.model.ReservationResult;
import com.kirin.reservation.model.ReservationTime;
import com.kirin.reservation.service.LineMessageService;
import com.kirin.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationTask {

  @Value("${kirin.target-name}")
  private String targetName;

  private final ReservationService reservationService;

  private final StartDateTimeConfig startDateTimeConfig;

  private final LineMessageService lineMessageService;

  @Scheduled(cron = "${cron}")
  public void executeReservation() throws MalformedURLException {
    // Todo:ZoneIdつけとく やっぱりTimeConfig欲しい
    LocalDateTime now = LocalDateTime.now(); // 実行日付を取得

    /**
     * バッチ実行時刻によって午前予約実施か午後予約実施かを判断する
     **/
    ReservationTime reservationTime;
    if (now.isBefore(startDateTimeConfig.getStartDateTimeAm())) {
      reservationTime = ReservationTime.AM;
    } else {
      reservationTime = ReservationTime.PM;
    }

    // DBに登録されている実行日の予約情報を取得
    ReservationDate reservationDate = reservationService.findReservationTarget(targetName, now.toLocalDate(),
        reservationTime);

    if (reservationDate.isEmpty()) {
      log.info("予約の予約がされてないよ！");
      return;
    }

    // 予約実行
    boolean isSuccess = reservationService.reserve(targetName);

    ReservationResult result = ReservationResult.of(reservationDate, isSuccess);

    // 各予約結果についてLINE通知を送信する
    lineMessageService.sendReservationResult(result);
  }

}
