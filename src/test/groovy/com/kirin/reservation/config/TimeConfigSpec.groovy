package com.kirin.reservation.config

import com.kirin.reservation.model.ReservationTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TimeConfig.class)
class TimeConfigSpec extends Specification {

    @Autowired
    TimeConfig timeConfig

    def "getNow_現在時刻取得"() {
        setup:
        def instant = Instant.parse("2022-11-23T12:12:46Z")
        def clock = Clock.fixed(instant, ZoneId.of("UTC"))

        when:
        def actual = timeConfig.getNow(clock)

        then:
        actual == LocalDateTime.of(2022, 11, 23, 12, 12, 46)
    }

    def "getNowDayOfWeek_現在曜日取得"() {
        setup:
        def instant = Instant.parse(time)
        def clock = Clock.fixed(instant, ZoneId.of("UTC"))

        when:
        def actual = timeConfig.getNowDayOfWeek(clock)

        then:
        actual == expected

        where:
        time                   | expected
        "2022-11-20T12:12:46Z" | DayOfWeek.SUNDAY
        "2022-11-21T12:12:46Z" | DayOfWeek.MONDAY
        "2022-11-22T12:12:46Z" | DayOfWeek.TUESDAY
        "2022-11-23T12:12:46Z" | DayOfWeek.WEDNESDAY
        "2022-11-24T12:12:46Z" | DayOfWeek.THURSDAY
        "2022-11-25T12:12:46Z" | DayOfWeek.FRIDAY
        "2022-11-26T12:12:46Z" | DayOfWeek.SATURDAY
    }

    def "getReservationTime_時間帯ドメインを取得"() {
        setup:
        def instant = Instant.parse(time)
        def clock = Clock.fixed(instant, ZoneId.of("UTC"))

        when:
        def actual = timeConfig.getReservationTime(clock)

        then:
        actual == expected

        where:
        time                   | expected
        "2022-11-20T06:50:46Z" | ReservationTime.AM
        "2022-11-20T06:59:59Z" | ReservationTime.AM
        "2022-11-20T07:00:00Z" | ReservationTime.PM
        "2022-11-20T07:12:46Z" | ReservationTime.PM
    }

    def "getTargetTime_予約歌詞時刻を取得"() {
        setup:
        def instant = Instant.parse(time)
        def clock = Clock.fixed(instant, ZoneId.of("UTC"))

        when:
        def actual = timeConfig.getTargetTime(reservationTime, clock)

        then:
        actual == expected

        where:
        time                   | reservationTime    | expected
        "2022-11-20T06:50:46Z" | ReservationTime.AM | LocalDateTime.of(2022, 11, 20, 7, 0, 0)
        "2022-11-20T09:50:46Z" | ReservationTime.AM | LocalDateTime.of(2022, 11, 20, 7, 0, 0)
        "2022-11-23T06:50:46Z" | ReservationTime.AM | LocalDateTime.of(2022, 11, 23, 7, 0, 0)
        "2022-11-23T06:50:46Z" | ReservationTime.PM | LocalDateTime.of(2022, 11, 23, 13, 0, 0)
        "2022-11-23T06:50:46Z" | ReservationTime.PM | LocalDateTime.of(2022, 11, 23, 13, 0, 0)
        "2022-11-23T14:50:46Z" | ReservationTime.PM | LocalDateTime.of(2022, 11, 23, 13, 0, 0)
    }

    /**
     * テスト不可
     */
//    def "until_予約開始まで待機"() {
//    }


}
