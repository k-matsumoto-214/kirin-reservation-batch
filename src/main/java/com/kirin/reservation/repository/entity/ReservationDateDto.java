package com.kirin.reservation.repository.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationDateDto {
  private String Name;
  private LocalDate date;
  private String reservationTime;
}
