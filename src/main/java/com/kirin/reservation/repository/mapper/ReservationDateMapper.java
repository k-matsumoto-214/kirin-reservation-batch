package com.kirin.reservation.repository.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kirin.reservation.repository.entity.ReservationDateDto;

@Mapper
public interface ReservationDateMapper {

  List<ReservationDateDto> findByReservationDate(LocalDate reservationDate);
}
