package com.kirin.reservation.repository.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.kirin.reservation.model.ReservationDateList;
import com.kirin.reservation.repository.ReservationDateRepository;
import com.kirin.reservation.repository.mapper.ReservationDateMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReservationDateRepositoryImpl implements ReservationDateRepository {

  private final ReservationDateMapper mapper;

  @Override
  public ReservationDateList findByReservationDate(LocalDate reservationDate) {
    return ReservationDateList.from(mapper.findByReservationDate(reservationDate));

  }
}
