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

  private static final String SUCCESS_MESSAGE = "$sの予約が完了しました!(%s) 順番は %s です。";

  private final LineMessagingClient lineMessagingClient;

  public boolean sendReservationResult(ReservationResult result) {

    TextMessage textMessage = new TextMessage(
        String.format(SUCCESS_MESSAGE, result.getName(), result.getFormattedDate(), result.getReservedOrder()));

    PushMessage pushMessage = new PushMessage(lineGroupId, textMessage);

    try {
      lineMessagingClient.pushMessage(pushMessage).get();
      return true;
    } catch (Exception e) {
      log.error("LINE通知に失敗しちゃった。。。 原因: {}", e);
      return false;
    }
  }

}
