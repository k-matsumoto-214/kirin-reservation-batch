package com.kirin.reservation.config

import com.kirin.reservation.model.ReservationTime
import org.openqa.selenium.By
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.Clock
import java.time.DayOfWeek

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = KirinWebConfig.class)
class KirinWebConfigSpec extends Specification {

    @Autowired
    KirinWebConfig kirinWebConfig

    @SpringBean
    TimeConfig timeConfig = Mock()

    def "emailSelector_メールセレクター取得"() {
        when:
        def actual = kirinWebConfig.emailSelector()

        then:
        actual == By.cssSelector("#email")
    }

    def "passwordSelector_パスワードセレクター取得"() {
        when:
        def actual = kirinWebConfig.passwordSelector()

        then:
        actual == By.cssSelector("#password")
    }

    def "loginSelector_ログインセレクター取得"() {
        when:
        def actual = kirinWebConfig.loginSelector()

        then:
        actual == By.cssSelector("#login_content > ul > li > input")
    }

    def "userIdSelector_ユーザIDセレクター取得"() {
        when:
        def actual = kirinWebConfig.userIdSelector()

        then:
        actual == By.cssSelector("#user_id_target-id")
    }

    def "executeSelector()_実行セレクター取得"() {
        when:
        def actual = kirinWebConfig.executeSelector()

        then:
        actual == By.cssSelector("#reserve > div > form > ul > li > input")
    }

    def "reservationOrderSelector()_予約順セレクター取得"() {
        when:
        def actual = kirinWebConfig.reservationOrderSelector()

        then:
        actual == By.cssSelector("#reserve > div > fieldset:nth-child(5) > div:nth-child(5) > div")
    }

    def "url_ログインページのURL取得"() {
        when:
        def actual = kirinWebConfig.url()

        then:
        actual == "url"
    }

    def "reservationUrl_予約ページのURL取得"() {
        setup:
        timeConfig.getNowDayOfWeek(_ as Clock) >> dayOfWeek

        when:
        def actual = kirinWebConfig.reservationUrl(reservationTime, Mock(Clock))

        then:
        actual == expected


        where:
        dayOfWeek           | reservationTime    | expected
        DayOfWeek.SUNDAY    | ReservationTime.AM | "test-url-am"
        DayOfWeek.MONDAY    | ReservationTime.AM | "test-url-am"
        DayOfWeek.TUESDAY   | ReservationTime.AM | "test-url-am"
        DayOfWeek.WEDNESDAY | ReservationTime.AM | "test-url-am"
        DayOfWeek.THURSDAY  | ReservationTime.AM | "test-url-am"
        DayOfWeek.FRIDAY    | ReservationTime.AM | "test-url-am"
        DayOfWeek.SATURDAY  | ReservationTime.AM | "test-url-am-sat"
        DayOfWeek.SUNDAY    | ReservationTime.PM | "test-url-pm"
        DayOfWeek.MONDAY    | ReservationTime.PM | "test-url-pm"
        DayOfWeek.TUESDAY   | ReservationTime.PM | "test-url-pm"
        DayOfWeek.WEDNESDAY | ReservationTime.PM | "test-url-pm"
        DayOfWeek.THURSDAY  | ReservationTime.PM | "test-url-pm"
        DayOfWeek.FRIDAY    | ReservationTime.PM | "test-url-pm"
        DayOfWeek.SATURDAY  | ReservationTime.PM | "test-url-pm-sat"
    }

    def "password_ログインパスワード取得"() {
        when:
        def actual = kirinWebConfig.password()

        then:
        actual == "password"
    }

    def "user_ユーザー取得"() {
        when:
        def actual = kirinWebConfig.user()

        then:
        actual == "user"
    }

}
