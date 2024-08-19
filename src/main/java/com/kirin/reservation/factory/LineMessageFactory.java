package com.kirin.reservation.factory;

import com.kirin.reservation.model.ReservationDate;
import com.kirin.reservation.model.ReservationResult;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.message.flex.unit.FlexPaddingSize;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LineMessageFactory {

  private static final String FAILURE_MESSAGE = "%sの予約に失敗しました...(%s %s) ";
  private static final String SUCCESS_TITLE = "予約が確定しました";
  private static final String SUCCESS_DETAIL = "%s(%s %s) : %d番";
  private static final String RESERVATION_DETAIL_LABEL = "予約詳細";
  private static final String RESERVATION_WEB_INTERVIEW_LABEL = "WEB問診票";
  private static final String ALT_TEXT = "予約確定通知(%s)";

  @Value("${kirin.detail-url}")
  private String reservationDetailUrl;
  @Value("${kirin.interview-url}")
  private String reservationWebInterviewUrl;

  /**
   * 予約失敗時の送信メッセージを作成する
   *
   * @param reservationDate 予約情報ドメイン
   * @return 送信メッセージ(TEXT)
   */
  public Message createFailureMessage(ReservationDate reservationDate) {

    return new TextMessage(
        String.format(FAILURE_MESSAGE,
            reservationDate.getName(),
            reservationDate.getFormattedDate(),
            reservationDate.getReservationTime().getDescription()));
  }

  /**
   * 予約成功時の送信メッセージを作成する
   *
   * @param result 予約結果ドメイン
   * @return 送信メッセージ(FLEX)
   */
  public Message createSuccessMessage(ReservationResult result) {
    final Text title = Text.builder()
        .text(SUCCESS_TITLE)
        .weight(TextWeight.BOLD)
        .size(FlexFontSize.XL)
        .build();

    final Text detail = Text.builder()
        .text(String.format(
            SUCCESS_DETAIL,
            result.getName(),
            result.getDate(),
            result.getReservationTime(),
            result.getReservationOrder()))
        .size(FlexFontSize.Md)
        .build();

    final Box bodyBlock = Box.builder()
        .layout(FlexLayout.VERTICAL)
        .spacing(FlexMarginSize.SM)
        .paddingBottom(FlexPaddingSize.NONE)
        .contents(List.of(title, detail))
        .build();

    final Button reservationDetailAction = Button
        .builder()
        .style(ButtonStyle.LINK)
        .height(ButtonHeight.SMALL)
        .style(ButtonStyle.PRIMARY)
        .action(new URIAction(RESERVATION_DETAIL_LABEL,
            URI.create(reservationDetailUrl),
            null))
        .build();

    final Button reservationWebInterviewAction = Button.builder()
        .style(ButtonStyle.LINK)
        .height(ButtonHeight.SMALL)
        .style(ButtonStyle.SECONDARY)
        .action(new URIAction(RESERVATION_WEB_INTERVIEW_LABEL,
            URI.create(reservationWebInterviewUrl),
            null))
        .build();

    final Box footerBlock = Box.builder()
        .layout(FlexLayout.VERTICAL)
        .spacing(FlexMarginSize.SM)
        .contents(List.of(reservationDetailAction, reservationWebInterviewAction))
        .build();

    final Bubble bubble = Bubble.builder()
        .body(bodyBlock)
        .footer(footerBlock)
        .build();

    final String altText = String.format(ALT_TEXT, result.getName());

    return new FlexMessage(altText, bubble);
  }

}
