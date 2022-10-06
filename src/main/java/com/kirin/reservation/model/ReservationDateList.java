package com.kirin.reservation.model;

import java.util.List;
import java.util.stream.Collectors;

import com.kirin.reservation.repository.entity.ReservationDateDto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class ReservationDateList {

  private final List<ReservationDate> reservationDates;

  /**
   * 予約日付モデルのリストモデルファクトリ
   * 
   * @param reservationDateDtos 予約日付DTOのリスト
   * @return 予約日付モデルのリストモデル
   */
  public static ReservationDateList from(List<ReservationDateDto> reservationDateDtos) {

    List<ReservationDate> reservationDates = reservationDateDtos.stream()
        .map(ReservationDate::from)
        .collect(Collectors.toUnmodifiableList());

    return ReservationDateList.builder()
        .reservationDates(reservationDates)
        .build();
  }

  /**
   * 重複を省いた予約日付情報のリストを返します
   * 
   * @return 重複を省いた予約日付情報のリスト
   */
  public List<ReservationDate> getDistinctReservationDates() {
    return this.reservationDates.stream()
        .distinct()
        .collect(Collectors.toUnmodifiableList());
  }

}
