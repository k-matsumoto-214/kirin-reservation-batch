package com.kirin.reservation.runner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.kirin.reservation.model.ReservationDate;
import com.kirin.reservation.model.ReservationDateList;
import com.kirin.reservation.model.ReservationResult;
import com.kirin.reservation.service.LineMessageService;
import com.kirin.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationApplicationRunner implements ApplicationRunner {

  private final ReservationService reservationService;

  private final LineMessageService lineMessageService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    LocalDate now = LocalDate.now(); // 実行日付を取得

    // DBに登録されている実行日の予約情報を取得
    ReservationDateList reservationDateList = reservationService.findReserVationTarget(now);

    // 重複を省いた予約日付情報を取得
    List<ReservationDate> reservationDates = reservationDateList.getDistinctReservationDates();

    if (reservationDates.isEmpty()) {
      log.info("予約の予約がされてないよ！");
      return;
    }

    // 予約者分予約処理を行う
    // 予約結果を保持するマップを定義
    List<ReservationResult> reservationResults = new ArrayList<>();

    for (ReservationDate reservationDate : reservationDates) {
      String targetName = reservationDate.getName();

      // 予約実行
      int reservedOrder = reservationService.reserve(targetName);

      // 予約結果を格納
      reservationResults.add(ReservationResult.of(targetName, now, reservedOrder));
    }

    // 各予約結果についてLINE通知を送信する
    reservationResults.forEach(result -> lineMessageService.sendReservationResult(result));
  }

}
