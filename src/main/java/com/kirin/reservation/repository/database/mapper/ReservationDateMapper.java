package com.kirin.reservation.repository.database.mapper;

import com.kirin.reservation.repository.database.entity.ReservationDateDto;
import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;

import com.kirin.reservation.model.ReservationTime;

@Mapper
public interface ReservationDateMapper {

  ReservationDateDto findByReservationDate(String targetName, LocalDate reservationDate,
      ReservationTime reservationTime);
}
