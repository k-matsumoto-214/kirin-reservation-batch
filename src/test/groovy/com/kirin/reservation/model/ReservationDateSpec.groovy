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

    def "getFormattedDate_フォーマットされた日付が取得できる"() {
        setup:
        def name = "name"
        def date = testValue
        def reservationTime = ReservationTime.AM

        def dto = Mock(ReservationDateDto) {
            getName() >> name
            getDate() >> date
            getReservationTime() >> reservationTime.getValue()
        }

        when:
        def actual = ReservationDate.from(dto)

        then:
        actual.getFormattedDate() == expected

        where:
        testValue                  | expected
        LocalDate.of(2020, 10, 30) | "2020年10月30日"
        LocalDate.of(2005, 9, 9)   | "2005年09月09日"
    }
}
