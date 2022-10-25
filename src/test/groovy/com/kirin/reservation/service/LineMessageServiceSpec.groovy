package com.kirin.reservation.service

import com.kirin.reservation.factory.LineMessageFactory
import com.kirin.reservation.model.ReservationDate
import com.kirin.reservation.model.ReservationResult
import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.model.PushMessage
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.response.BotApiResponse
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = LineMessageService.class)
class LineMessageServiceSpec extends Specification {

    @Autowired
    LineMessageService lineMessageService
    @SpringBean
    LineMessagingClient lineMessagingClient = Mock()
    @SpringBean
    LineMessageFactory lineMessageFactory = Mock()

    def "sendSuccessMessage_予約成功を通知"() {
        setup:
        def result = Mock(ReservationResult)

        1 * lineMessageFactory.createSuccessMessage(_) >> GroovyMock(Message)

        1 * lineMessagingClient.pushMessage(_ as PushMessage) >>
                Mock(CompletableFuture<BotApiResponse>) {
                    get() >> GroovyMock(BotApiResponse)
                }

        when:
        lineMessageService.sendSuccessMessage(result)

        then:
        noExceptionThrown()
    }

    def "sendSuccessMessage_通知失敗"() {
        setup:
        def result = Mock(ReservationResult)

        1 * lineMessageFactory.createSuccessMessage(_) >> GroovyMock(Message)

        1 * lineMessagingClient.pushMessage(_ as PushMessage) >> {
            throw new RuntimeException()
        }

        when:
        lineMessageService.sendSuccessMessage(result)

        then:
        noExceptionThrown()
    }

    def "sendFailureMessage_予約失敗を通知"() {
        setup:
        def reservationDate = Mock(ReservationDate)

        1 * lineMessageFactory.createFailureMessage(_) >> GroovyMock(Message)

        1 * lineMessagingClient.pushMessage(_ as PushMessage) >>
                Mock(CompletableFuture<BotApiResponse>) {
                    get() >> GroovyMock(BotApiResponse)
                }

        when:
        lineMessageService.sendFailureMessage(reservationDate)

        then:
        noExceptionThrown()
    }

    def "sendFailureMessage_通知失敗"() {
        setup:
        def reservationDate = Mock(ReservationDate)

        1 * lineMessageFactory.createFailureMessage(_) >> GroovyMock(Message)

        1 * lineMessagingClient.pushMessage(_ as PushMessage) >> {
            throw new RuntimeException()
        }

        when:
        lineMessageService.sendFailureMessage(reservationDate)

        then:
        noExceptionThrown()
    }
}
