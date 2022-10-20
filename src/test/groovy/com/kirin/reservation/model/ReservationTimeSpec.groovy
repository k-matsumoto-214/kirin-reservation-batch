package com.kirin.reservation.model

import spock.lang.Specification

class ReservationTimeSpec extends Specification {

    def "from_ファクトリ"() {
        when:
        def actual = ReservationTime.from(testValue)

        then:
        actual == expected

        where:
        testValue | expected
        "0"       | ReservationTime.AM
        "1"       | ReservationTime.PM
    }

    def "from_ファクトリ_引数が不正"() {
        when:
        def actual = ReservationTime.from(testValue)

        then:
        thrown(IllegalArgumentException)

        where:
        pattern  | testValue
        "空文字" | ""
        "null"   | null
        "不正値" | "3"
    }
}
