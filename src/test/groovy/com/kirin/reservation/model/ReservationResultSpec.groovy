package com.kirin.reservation.model

import spock.lang.Specification

class ReservationResultSpec extends Specification {

    def "of_ファクトリ"() {
        setup:
        def name = "name"
        def date = "2020年10月30日"
        def reservationTime = ReservationTime.AM
        def reservationOrder = 1

        def reservationDate = Mock(ReservationDate) {
            getName() >> name
            getFormattedDate() >> date
            getReservationTime() >> reservationTime
        }

        when:
        def actual = ReservationResult.of(reservationDate, reservationOrder)

        then:
        actual.getName() == name
        actual.getDate() == date
        actual.getReservationTime() == reservationTime.getDescription()
        actual.getReservationOrder() == reservationOrder
    }
}
