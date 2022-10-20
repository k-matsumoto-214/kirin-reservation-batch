package com.kirin.reservation.model

import spock.lang.Specification

import java.time.LocalDate

class ReservationResultSpec extends Specification {

    def "of_ファクトリ"() {
        setup:
        def name = "name"
        def date = LocalDate.of(2020, 10, 30)
        def reservationTime = ReservationTime.AM

        def reservationDate = Mock(ReservationDate) {
            getName() >> name
            getDate() >> date
            getReservationTime() >> reservationTime
        }

        when:
        def actual = ReservationResult.of(reservationDate, true)

        then:
        actual.getName() == name
        actual.getDate() == date
        actual.getReservationTime() == reservationTime
        actual.isSuccess() == true
    }

    def "getFormattedDate_フォーマットされた日付が取得できる"() {
        setup:
        def name = "name"
        def date = testValue
        def reservationTime = ReservationTime.AM

        def reservationDate = Mock(ReservationDate) {
            getName() >> name
            getDate() >> date
            getReservationTime() >> reservationTime
        }

        when:
        def actual = ReservationResult.of(reservationDate, true)

        then:
        actual.getFormattedDate() == expected

        where:
        testValue                  | expected
        LocalDate.of(2020, 10, 30) | "2020年10月30日"
        LocalDate.of(2005, 9, 9)   | "2005年09月09日"
    }
}
