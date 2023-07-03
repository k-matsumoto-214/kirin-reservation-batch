package com.kirin.reservation.service

import com.kirin.reservation.config.KirinWebConfig
import com.kirin.reservation.config.TimeConfig
import com.kirin.reservation.config.WebDriverConfig
import com.kirin.reservation.model.ReservationDate
import com.kirin.reservation.model.ReservationTime
import com.kirin.reservation.repository.database.ReservationDateRepository
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.retry.annotation.EnableRetry
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ReservationService.class)
@EnableRetry
class ReservationServiceSpec extends Specification {

    @Autowired
    ReservationService reservationService

    @SpringBean
    ReservationDateRepository reservationDateRepository = Mock()

    @SpringBean
    WebDriverConfig webDriverConfig = Mock()

    @SpringBean
    TimeConfig timeConfig = Mock()

    @SpringBean
    KirinWebConfig kirinWebConfig = Mock()

    def "findReservationTarget_予約情報を取得できる"() {
        setup:
        1 * reservationDateRepository.findByReservationDate(*_) >> Mock(ReservationDate)

        when:
        def actual =
                reservationService.findReservationTarget(
                        "name", GroovyMock(LocalDate), GroovyMock(ReservationTime))

        then:
        !actual.isEmpty()

    }

    def "findReservationTarget_失敗時リトライする"() {
        setup:
        3 * reservationDateRepository.findByReservationDate(*_) >> {
            throw new RuntimeException()
        }

        when:
        def actual =
                reservationService.findReservationTarget(
                        "name", GroovyMock(LocalDate), GroovyMock(ReservationTime))

        then:
        thrown(RuntimeException)
    }

    def "reserve_正常処理"() {
        setup:
        def reservationOrderString = "3"

        1 * webDriverConfig.getWebDriver() >> Mock(RemoteWebDriver) {
            navigate() >> Mock(WebDriver.Navigation)
            findElement(_ as By) >> Mock(WebElement) {
                isSelected() >> false
                getText() >> reservationOrderString
            }
            switchTo() >> Mock(WebDriver.TargetLocator) {
                alert() >> Mock(Alert)
            }
        }

        1 * timeConfig.getTargetTime(*_) >> GroovyMock(LocalDateTime)
        1 * timeConfig.until(*_)

        1 * kirinWebConfig.emailSelector() >> Mock(By)
        1 * kirinWebConfig.passwordSelector() >> Mock(By)
        1 * kirinWebConfig.loginSelector() >> Mock(By)
        2 * kirinWebConfig.userIdSelector() >> Mock(By)
        1 * kirinWebConfig.executeSelector() >> Mock(By)
        1 * kirinWebConfig.reservationOrderSelector() >> Mock(By)


        when:
        def actual = reservationService.reserve("name", GroovyMock(ReservationTime))

        then:
        actual == Integer.parseInt(reservationOrderString)
    }

    def "reserve_例外発生"() {
        setup:
        1 * webDriverConfig.getWebDriver() >> Mock(RemoteWebDriver) {
            navigate() >> Mock(WebDriver.Navigation)
            findElement(_ as By) >> Mock(WebElement) {
                isSelected() >> true
                getText() >> "0"
            }
            switchTo() >> Mock(WebDriver.TargetLocator) {
                alert() >> {
                    throw new RuntimeException()
                }
            }
        }

        1 * kirinWebConfig.emailSelector() >> Mock(By)
        1 * kirinWebConfig.passwordSelector() >> Mock(By)
        1 * kirinWebConfig.loginSelector() >> Mock(By)
        1 * kirinWebConfig.userIdSelector() >> Mock(By)
        1 * kirinWebConfig.executeSelector() >> Mock(By)
        0 * kirinWebConfig.reservationOrderSelector()

        when:
        reservationService.reserve("name", GroovyMock(ReservationTime))

        then:
        thrown(RuntimeException)
    }
}
