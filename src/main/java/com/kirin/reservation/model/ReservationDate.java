package com.kirin.reservation.model;

import com.kirin.reservation.repository.database.entity.ReservationDateDto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class ReservationDate {

  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
  private final String name;
  private final LocalDate date;
  
  private final ReservationTime reservationTime;

  /**
   * 予約日付モデルのファクトリ
   *
   * @param dto DBから取得した予約日付DTO
   * @return 予約日付モデル
   */
  public static ReservationDate from(ReservationDateDto dto) {
    if (Objects.isNull(dto)) {
      return ReservationDate.empty();
    }

    return ReservationDate.builder()
        .name(dto.getName())
        .date(dto.getDate())
        .reservationTime(ReservationTime.from(dto.getReservationTime()))
        .build();
  }

  /**
   * 空ドメイン生成
   *
   * @return 空の予約情報日付ドメイン
   */
  public static ReservationDate empty() {
    return ReservationDate.builder()
        .name("")
        .date(null)
        .reservationTime(null)
        .build();
  }

  /**
   * 空ドメイン判定
   *
   * @return ドメインが空の時true
   */
  public boolean isEmpty() {
    return Objects.equals(this.name, "");
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
