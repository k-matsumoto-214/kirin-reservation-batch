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
  private final int reservedOrder;

  /**
   * ファクトリメソッド
   * 
   * @param name          予約者名
   * @param date          予約日付
   * @param reservedOrder 受付順
   * @return 予約結果オブジェクト
   */
  public static ReservationResult of(String name, LocalDate date, int reservedOrder) {
    return ReservationResult.builder()
        .name(name)
        .date(date)
        .reservedOrder(reservedOrder)
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
