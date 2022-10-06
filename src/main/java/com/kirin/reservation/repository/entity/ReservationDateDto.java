package com.kirin.reservation.repository.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationDateDto {
  private int id;
  private String Name;
  private LocalDate date;
}
