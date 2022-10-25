package com.kirin.reservation.model

import spock.lang.Specification

import java.time.LocalDate

class ReservationResultSpec extends Specification {

    def "of_ファクトリ"() {
        setup:
        def name = "name"
        def date = LocalDate.of(2020, 10, 30)
        def reservationTime = ReservationTime.AM
        def reservationOrder = 1

        def reservationDate = Mock(ReservationDate) {
            getName() >> name
            getDate() >> date
            getReservationTime() >> reservationTime
        }

        when:
        def actual = ReservationResult.of(reservationDate, reservationOrder)

        then:
        actual.getName() == name
        actual.getDate() == date
        actual.getReservationTime() == reservationTime
        actual.getReservationOrder() == reservationOrder
    }
}
