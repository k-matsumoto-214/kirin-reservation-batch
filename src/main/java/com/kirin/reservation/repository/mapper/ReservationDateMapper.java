package com.kirin.reservation.repository.mapper;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;

import com.kirin.reservation.model.ReservationTime;
import com.kirin.reservation.repository.entity.ReservationDateDto;

@Mapper
public interface ReservationDateMapper {

  ReservationDateDto findByReservationDate(String targetName, LocalDate reservationDate,
      ReservationTime reservationTime);
}
