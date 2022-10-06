package com.kirin.reservation.model;

import java.time.LocalDate;
import java.util.Objects;

import com.kirin.reservation.repository.entity.ReservationDateDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class ReservationDate {

  private final int id;
  private final String name;
  private final LocalDate date;

  /**
   * 予約日付モデルのファクトリ
   * 
   * @param dto DBから取得した予約日付DTO
   * @return 予約日付モデル
   */
  public static ReservationDate from(ReservationDateDto dto) {
    return ReservationDate.builder()
        .id(dto.getId())
        .name(dto.getName())
        .date(dto.getDate())
        .build();
  }

  @Override
  /**
   * ReservationDateオブジェクトの比較
   * name, dateプロパティを参照して等しいかどうか判断します
   * name, dateプロパティが等価であるときtrue
   */
  public boolean equals(Object obj) {
    if (obj instanceof ReservationDate) {
      ReservationDate reservationDate = (ReservationDate) obj;
      return Objects.equals(this.getName(), reservationDate.getName())
          && Objects.equals(this.getDate(), reservationDate.getDate());
    }
    return false;
  }
}
