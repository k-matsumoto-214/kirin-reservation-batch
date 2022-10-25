package com.kirin.reservation.factory

import com.kirin.reservation.model.ReservationDate
import com.kirin.reservation.model.ReservationResult
import com.kirin.reservation.model.ReservationTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = LineMessageFactory)
class LineMessageFactorySpec extends Specification {

    @Autowired
    LineMessageFactory lineMessageFactory

    def "createFailureMessage_正常"() {
        setup:
        def name = "name"
        def date = "2022年12月10日"
        def reservationTime = ReservationTime.AM

        def reservationDate = Mock(ReservationDate) {
            getName() >> name
            getFormattedDate() >> date
            getReservationTime() >> reservationTime
        }

        when:
        lineMessageFactory.createFailureMessage(reservationDate)

        then:
        noExceptionThrown()
    }

    def "createSuccessMessage_正常"() {
        setup:
        def name = "name"
        def dateString = "2022年12月10日"
        def reservationTime = ReservationTime.AM

        def result = Mock(ReservationResult) {
            getName() >> name
            getFormattedDate() >> dateString
            getReservationTime() >> reservationTime
        }

        when:
        lineMessageFactory.createSuccessMessage(result)

        then:
        noExceptionThrown()
    }


}
