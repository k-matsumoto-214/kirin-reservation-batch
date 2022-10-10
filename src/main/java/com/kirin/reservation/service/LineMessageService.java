package com.kirin.reservation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kirin.reservation.model.ReservationResult;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LineMessageService {

  @Value("${line.bot.group-id}")
  private String lineGroupId;

  private static final String SUCCESS_MESSAGE = "%sの予約が完了しました!(%s %s) ";
  private static final String FAILURE_MESSAGE = "%sの予約に失敗しました...(%s %s) ";

  private final LineMessagingClient lineMessagingClient;

  public boolean sendReservationResult(ReservationResult result) {

    String message;
    if (result.isSuccess()) {
      message = SUCCESS_MESSAGE;
    } else {
      message = FAILURE_MESSAGE;
    }

    TextMessage textMessage = new TextMessage(
        String.format(message, result.getName(), result.getFormattedDate(),
            result.getReservationTime().getDiscription()));

    PushMessage pushMessage = new PushMessage(lineGroupId, textMessage);

    try {
      lineMessagingClient.pushMessage(pushMessage).get();
      return true;
    } catch (Exception e) {
      log.error("LINE通知に失敗しちゃった。。。 原因: {}", e.toString());
      return false;
    }
  }

}
