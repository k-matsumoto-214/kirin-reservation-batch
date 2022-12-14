package com.kirin.reservation.service;

import com.kirin.reservation.factory.LineMessageFactory;
import com.kirin.reservation.model.ReservationDate;
import com.kirin.reservation.model.ReservationResult;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LineMessageService {

  @Value("${line.bot.group-id}")
  private String lineGroupId;

  private final LineMessagingClient lineMessagingClient;
  private final LineMessageFactory lineMessageFactory;

  public void sendSuccessMessage(ReservationResult result) {

    Message message = lineMessageFactory.createSuccessMessage(result);

    PushMessage pushMessage = new PushMessage(lineGroupId, message);

    try {
      lineMessagingClient.pushMessage(pushMessage).get();
    } catch (Exception e) {
      log.error("LINE通知に失敗しちゃった。。。 原因: {}", e.toString());
    }
  }

  public void sendFailureMessage(ReservationDate reservationDate) {

    Message message = lineMessageFactory.createFailureMessage(reservationDate);

    PushMessage pushMessage = new PushMessage(lineGroupId, message);

    try {
      lineMessagingClient.pushMessage(pushMessage).get();
    } catch (Exception e) {
      log.error("LINE通知に失敗しちゃった。。。 原因: {}", e.toString());
    }
  }
}
