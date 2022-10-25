package com.kirin.reservation.config;

import com.kirin.reservation.model.ReservationTime;
import java.util.Objects;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KirinWebConfig {

  @Value("${kirin.password}")
  private String kirinPassword;

  @Value("${kirin.user}")
  private String kirinUser;

  @Value("${kirin.url}")
  private String kirinUrl;

  @Value("${kirin.reservation-url.am}")
  private String kirinReservationUrlAM;

  @Value("${kirin.reservation-url.pm}")
  private String kirinReservationUrlPm;

  @Value("${kirin.target-id}")
  private String targetUserId;

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
   * @return 予約ページのURL
   */
  public String reservationUrl(ReservationTime reservationTime) {
    if (Objects.equals(reservationTime, ReservationTime.AM)) {
      return this.kirinReservationUrlAM;
    }

    return this.kirinReservationUrlPm;
  }

  public String password() {
    return this.kirinPassword;
  }

  public String user() {
    return this.kirinUser;
  }


}
