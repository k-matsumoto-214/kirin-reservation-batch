package com.kirin.reservation.model

import com.kirin.reservation.repository.database.entity.ReservationDateDto
import spock.lang.Specification

import java.time.LocalDate

class ReservationDateSpec extends Specification {

    def "from_ファクトリ"() {
        setup:
        def date = LocalDate.of(2020, 10, 1)
        def name = "name"
        def reservationTime = ReservationTime.PM

        def dto = Mock(ReservationDateDto) {
            getReservationTime() >> reservationTime.getValue()
            getName() >> name
            getDate() >> date
        }

        when:
        def actual = ReservationDate.from(dto)

        then:
        actual.getDate() == date
        actual.getName() == name
        actual.getReservationTime() == reservationTime
    }

    def "from_ファクトリ_引数が不正"() {
        when:
        def actual = ReservationDate.from(null)

        then:
        actual.isEmpty() == true
        actual.getName() == ""
    }

    def "empty_空ドメイン生成"() {
        when:
        def actual = ReservationDate.empty()

        then:
        actual.isEmpty() == true
        actual.getName() == ""
    }

    def "isEmpty_空ドメイン判定"() {
        setup:
        def date = LocalDate.of(2020, 10, 1)
        def name = testValue
        def reservationTime = ReservationTime.PM

        def dto = Mock(ReservationDateDto) {
            getReservationTime() >> reservationTime.getValue()
            getName() >> name
            getDate() >> date
        }

        when:
        def actual = ReservationDate.from(dto)

        then:
        actual.isEmpty() == expected

        where:
        testValue  | expected
        ""         | true
        "testName" | false
        null       | false
    }
}
