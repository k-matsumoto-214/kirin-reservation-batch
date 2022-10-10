package com.kirin.reservation.repository;

import java.time.LocalDate;

import com.kirin.reservation.model.ReservationDate;
import com.kirin.reservation.model.ReservationTime;

public interface ReservationDateRepository {

  /**
   * 予約日をキーにしてDBから予約情報を取得する
   * 
   * @param reservationDate 検索対象の日付
   * @return 予約日付情報のモデル
   */
  ReservationDate findByReservationDate(String targetName, LocalDate reservationDate, ReservationTime reservationTime);
}
