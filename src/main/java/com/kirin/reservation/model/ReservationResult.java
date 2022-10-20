package com.kirin.reservation.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class ReservationResult {

  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

  private final String name;
  private final LocalDate date;
  private final ReservationTime reservationTime;
  private final boolean isSuccess;

  /**
   * ファクトリメソッド
   *
   * @param reservationDate 予約情報ドメイン
   * @param isSuccess       予約結果
   * @return 予約結果オブジェクト
   */
  public static ReservationResult of(ReservationDate reservationDate, boolean isSuccess) {
    return ReservationResult.builder()
        .name(reservationDate.getName())
        .date(reservationDate.getDate())
        .reservationTime(reservationDate.getReservationTime())
        .isSuccess(isSuccess)
        .build();
  }

  /**
   * yyyy年MM月dd日フォーマットの予約日付文字列を返す
   *
   * @return フォーマットされた予約日付文字列
   */
  public String getFormattedDate() {
    return this.date.format(DTF);
  }
}
