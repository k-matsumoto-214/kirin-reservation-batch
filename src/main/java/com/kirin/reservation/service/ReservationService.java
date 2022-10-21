package com.kirin.reservation.service;

import com.kirin.reservation.config.TimeConfig;
import com.kirin.reservation.config.WebDriverConfig;
import com.kirin.reservation.model.ReservationDate;
import com.kirin.reservation.model.ReservationTime;
import com.kirin.reservation.repository.database.ReservationDateRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationService {

  @Value("${kirin.password}")
  private String kirinPassword;

  @Value("${kirin.user}")
  private String kirinUser;

  @Value("${kirin.url}")
  private String kirinUrl;

  @Value("${kirin.target-id}")
  private String targetUserId;

  private final ReservationDateRepository reservationDateRepository;

  private final WebDriverConfig webDriverConfig;

  private final TimeConfig timeConfig;

  /**
   * DBから予約情報を取得する
   *
   * @param targetName      予約対象者名
   * @param targeDate       予約対象日付
   * @param reservationTime 予約対象時間帯
   * @return 予約情報
   */
  @Retryable
  public ReservationDate findReservationTarget(String targetName, LocalDate targeDate,
      ReservationTime reservationTime) {

    // DBから予約情報を取得
    return reservationDateRepository.findByReservationDate(targetName, targeDate, reservationTime);
  }

  /**
   * web予約をおこなってその結果を返す
   *
   * @param targetName      予約対象者名
   * @param reservationTime 予約時間帯
   * @return 予約成功のときtrue
   */
  public boolean reserve(String targetName, ReservationTime reservationTime) {
    WebDriver webDriver = webDriverConfig.getWebDriver();
    WebDriverWait webDriverWait = webDriverConfig.getWebDriverWait(webDriver);

    try {
      log.info("{}の予約を開始", targetName);
      webDriver.get(kirinUrl);

      webDriverWait.until(driver -> driver.getTitle().toLowerCase().startsWith("ログイン"));

      webDriver.findElement(By.cssSelector("#email")).sendKeys(kirinUser);
      webDriver.findElement(By.cssSelector("#password")).sendKeys(kirinPassword);
      webDriver.findElement(By.cssSelector("#login_content > ul > li > input")).click();
      log.info("ログイン");

      // 予約時間になるまで待機
      int waitCount = 0;

      // 予約開始時間まで待機
      timeConfig.until(reservationTime);

      log.info("予約開始");
      webDriver.navigate().refresh();

      // 予約ボタンが表示されるまで待機
      webDriverWait.until(driver -> driver
          .findElement(
              By.cssSelector(
                  "#reserve_show_periods_1 > table > tbody > tr.row-available > td:nth-child(6) > a"))
          .isDisplayed());

      webDriver
          .findElement(
              By.cssSelector(
                  "#reserve_show_periods_1 > table > tbody > tr.row-available > td:nth-child(6) > a"))
          .click();

      // 予約対象者が表示されるまで待機
      String userIdSelector = "#user_id_" + targetUserId;
      webDriverWait.until(driver -> driver.findElement(By.cssSelector(userIdSelector))
          .isDisplayed());

      // 予約対象者にチェックする
      if (!webDriver.findElement(By.cssSelector(userIdSelector)).isSelected()) {
        webDriver.findElement(By.cssSelector(userIdSelector)).click();
      }

      // 予約実行
      webDriver.findElement(By.cssSelector("#reserve > div > form > ul > li > input")).click();

      // 予約確認のアラートが表示されるまで待機する
      webDriverWait.until(ExpectedConditions.alertIsPresent());
      webDriver.switchTo().alert().accept();

      return true;

    } catch (Exception e) {

      log.error("{}の予約中にエラー発生 原因: {}", targetName, e.toString());

      return false;

    } finally {
      webDriver.close();
    }
  }
}
