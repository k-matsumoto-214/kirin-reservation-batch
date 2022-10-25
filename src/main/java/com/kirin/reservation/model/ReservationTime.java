package com.kirin.reservation.model;

import com.google.common.base.Objects;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReservationTime {
  AM("午前", "0"), // 午前予約
  PM("午後", "1"); // 午後予約

  private final String description;
  private final String value;

  /**
   * ファクトリメソッド
   *
   * @param value 予約開始種別の値
   * @return 予約種別ドメイン
   */
  public static ReservationTime from(String value) {
    return Arrays.stream(ReservationTime.values())
        .filter(reservationTime -> Objects.equal(reservationTime.getValue(), value))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
