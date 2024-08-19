package com.kirin.reservation.config;

import com.kirin.reservation.model.ReservationTime;
import java.time.Clock;
import java.time.DayOfWeek;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KirinWebConfig {

  @Value("${kirin.password}")
  private String kirinPassword;

  @Value("${kirin.user}")
  private String kirinUser;

  @Value("${kirin.url}")
  private String kirinUrl;

  @Value("${kirin.reservation-url.am}")
  private String kirinReservationUrlAm;

  @Value("${kirin.reservation-url.pm}")
  private String kirinReservationUrlPm;

  @Value("${kirin.reservation-url.am-sat}")
  private String kirinReservationUrlAmSat;
  @Value("${kirin.reservation-url.pm-sat}")
  private String kirinReservationUrlPmSat;

  @Value("${kirin.target-id}")
  private String targetUserId;

  private final TimeConfig timeConfig;

  private static final String SELECTOR_EMAIL = "#email";
  private static final String SELECTOR_PASSWORD = "#password";
  private static final String SELECTOR_LOGIN = "#login_content > ul > li > input";

  private static final String SELECTOR_USER_ID = "#user_id_%s";

  private static final String SELECTOR_EXECUTE = "#reserve > div > form > ul > li > input";

  private static final String SELECTOR_RESERVATION_ORDER =
      "#reserve > div > fieldset:nth-child(5) > div:nth-child(5) > div";

  public By emailSelector() {
    return By.cssSelector(SELECTOR_EMAIL);
  }

  public By passwordSelector() {
    return By.cssSelector(SELECTOR_PASSWORD);
  }

  public By loginSelector() {
    return By.cssSelector(SELECTOR_LOGIN);
  }

  public By userIdSelector() {
    return By.cssSelector(String.format(SELECTOR_USER_ID, this.targetUserId));
  }

  public By executeSelector() {
    return By.cssSelector(SELECTOR_EXECUTE);
  }

  public By reservationOrderSelector() {
    return By.cssSelector(SELECTOR_RESERVATION_ORDER);
  }

  /**
   * きりんログインページのURLを返す
   *
   * @return ログインページのURL
   */
  public String url() {
    return this.kirinUrl;
  }

  /**
   * 予約ページのURLを返す
   *
   * @param reservationTime 予約時間帯
   * @param clock
   * @return 予約ページのURL
   */
  public String reservationUrl(ReservationTime reservationTime, Clock clock) {
    boolean isSat = Objects.equals(timeConfig.getNowDayOfWeek(clock), DayOfWeek.SATURDAY);
    boolean isAm = Objects.equals(reservationTime, ReservationTime.AM);
    if (isSat) {
      // 土曜日
      if (isAm) {
        // 午前
        return this.kirinReservationUrlAmSat;
      } else {
        // 午後
        return this.kirinReservationUrlPmSat;
      }
    } else {
      // 平日
      if (isAm) {
        // 午前
        return this.kirinReservationUrlAm;
      } else {
        // 午後
        return this.kirinReservationUrlPm;
      }
    }
  }

  public String password() {
    return this.kirinPassword;
  }

  public String user() {
    return this.kirinUser;
  }


}
