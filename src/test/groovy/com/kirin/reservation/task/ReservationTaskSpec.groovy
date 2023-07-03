package com.kirin.reservation.task

import com.kirin.reservation.config.TimeConfig
import com.kirin.reservation.model.ReservationDate
import com.kirin.reservation.model.ReservationTime
import com.kirin.reservation.service.LineMessageService
import com.kirin.reservation.service.ReservationService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.Clock
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ReservationTask.class)
class ReservationTaskSpec extends Specification {

    @Autowired
    ReservationTask reservationTask

    @SpringBean
    ReservationService reservationService = Mock()
    @SpringBean
    TimeConfig timeConfig = Mock()
    @SpringBean
    LineMessageService lineMessageService = Mock()
    @SpringBean
    Clock clock = Mock()

    def "executeReservation_正常"() {
        setup:
        1 * timeConfig.getNow(clock) >> LocalDateTime.now()
        1 * timeConfig.getReservationTime(_) >> GroovyMock(ReservationTime)
        1 * reservationService.findReservationTarget(*_) >> Mock(ReservationDate) {
            getReservationTime() >> GroovyMock(ReservationTime)
        }
        1 * reservationService.reserve(*_) >> 1
        1 * lineMessageService.sendSuccessMessage(_)

        when:
        reservationTask.executeReservation()

        then:
        noExceptionThrown()

    }

    def "executeReservation_異常"() {
        setup:
        1 * timeConfig.getNow(clock) >> LocalDateTime.now()
        1 * timeConfig.getReservationTime(_) >> GroovyMock(ReservationTime)
        1 * reservationService.findReservationTarget(*_) >> Mock(ReservationDate)
        1 * reservationService.reserve(*_) >> {
            throw new RuntimeException()
        }
        1 * lineMessageService.sendFailureMessage(_)

        when:
        reservationTask.executeReservation()

        then:
        noExceptionThrown()

    }

    def "executeReservation_正常_予約なし"() {
        setup:
        1 * timeConfig.getNow(clock) >> LocalDateTime.now()
        1 * timeConfig.getReservationTime(_) >> GroovyMock(ReservationTime)
        1 * reservationService.findReservationTarget(*_) >> Mock(ReservationDate) {
            isEmpty() >> true
        }
        0 * reservationService.reserve(*_) >> true
        0 * lineMessageService.sendSuccessMessage(_)

        when:
        reservationTask.executeReservation()

        then:
        noExceptionThrown()

    }
}
