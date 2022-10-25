package com.kirin.reservation.service

import com.kirin.reservation.config.KirinWebConfig
import com.kirin.reservation.config.TimeConfig
import com.kirin.reservation.config.WebDriverConfig
import com.kirin.reservation.model.ReservationDate
import com.kirin.reservation.model.ReservationTime
import com.kirin.reservation.repository.database.ReservationDateRepository
import org.openqa.selenium.*
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.retry.annotation.EnableRetry
import spock.lang.Specification

import java.nio.file.Path
import java.time.LocalDate

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
        1 * webDriverConfig.getWebDriver() >> Mock(RemoteWebDriver) {
            navigate() >> Mock(WebDriver.Navigation)
            findElement(_ as By) >> Mock(WebElement) {
                isSelected() >> false
            }
            switchTo() >> Mock(WebDriver.TargetLocator) {
                alert() >> Mock(Alert)
            }
            getScreenshotAs(_ as OutputType<Object>) >> Mock(File) {
                toPath() >> Mock(Path)
            }
        }

        1 * webDriverConfig.getWebDriverWait(_) >> Mock(WebDriverWait) {
            until({}) >> true

        }

        1 * timeConfig.until(_)

        1 * kirinWebConfig.emailSelector() >> Mock(By)
        1 * kirinWebConfig.passwordSelector() >> Mock(By)
        1 * kirinWebConfig.loginSelector() >> Mock(By)
        1 * kirinWebConfig.reserveSelector() >> Mock(By)
        2 * kirinWebConfig.userIdSelector() >> Mock(By)
        1 * kirinWebConfig.executeSelector() >> Mock(By)


        when:
        def actual = reservationService.reserve("name", GroovyMock(ReservationTime))

        then:
        actual == true
    }

    def "reserve_例外発生"() {
        setup:
        1 * webDriverConfig.getWebDriver() >> Mock(RemoteWebDriver) {
            navigate() >> Mock(WebDriver.Navigation)
            findElement(_ as By) >> Mock(WebElement) {
                isSelected() >> true
            }
            switchTo() >> Mock(WebDriver.TargetLocator) {
                alert() >> {
                    throw new RuntimeException()
                }
            }
            getScreenshotAs(_ as OutputType<Object>) >> Mock(File)
        }

        1 * webDriverConfig.getWebDriverWait(_) >> Mock(WebDriverWait) {
            until({}) >> true

        }

        1 * timeConfig.until(_)

        1 * kirinWebConfig.emailSelector() >> Mock(By)
        1 * kirinWebConfig.passwordSelector() >> Mock(By)
        1 * kirinWebConfig.loginSelector() >> Mock(By)
        1 * kirinWebConfig.reserveSelector() >> Mock(By)
        1 * kirinWebConfig.userIdSelector() >> Mock(By)
        1 * kirinWebConfig.executeSelector() >> Mock(By)

        when:
        def actual = reservationService.reserve("name", GroovyMock(ReservationTime))

        then:
        actual == false
    }
}
