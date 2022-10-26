package com.kirin.reservation.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class ReservationResult {


  private final String name;
  private final String date;
  private final String reservationTime;
  private final int reservationOrder;

  /**
   * ファクトリメソッド
   *
   * @param reservationDate  予約情報ドメイン
   * @param reservationOrder 予約受付順序
   * @return 予約結果オブジェクト
   */
  public static ReservationResult of(ReservationDate reservationDate, int reservationOrder) {
    return ReservationResult.builder()
        .name(reservationDate.getName())
        .date(reservationDate.getFormattedDate())
        .reservationTime(reservationDate.getReservationTime().getDescription())
        .reservationOrder(reservationOrder)
        .build();
  }


}
