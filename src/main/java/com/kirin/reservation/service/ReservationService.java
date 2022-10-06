package com.kirin.reservation.service;

import java.net.MalformedURLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.kirin.reservation.config.StartDateTimeConfig;
import com.kirin.reservation.model.Name;
import com.kirin.reservation.model.ReservationDateList;
import com.kirin.reservation.repository.ReservationDateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

  private final ReservationDateRepository reservationDateRepository;

  private final RemoteWebDriver webDriver;

  private final StartDateTimeConfig startDateTimeConfig;

  /**
   * 予約処理を実行しLINEに結果を通知する
   * 
   * @param targeDate 処理実行日
   * @throws MalformedURLException
   * @throws InterruptedException
   */
  @Retryable
  public ReservationDateList findReserVationTarget(LocalDate targeDate) {

    // DBから予約情報を取得
    return reservationDateRepository.findByReservationDate(targeDate);
  }

  /**
   * web予約をおこなって受付番号を返す
   * 
   * @param targetName 予約対象者名
   * @return 予約受付番号
   * @throws MalformedURLException
   */
  public int reserve(String targetName) {
    try {
      log.info("{}の予約を開始", targetName);
      webDriver.get(kirinUrl);

      WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(10), Duration.ofMillis(30));
      webDriverWait.until(webDriver -> webDriver.getTitle().toLowerCase().startsWith("ログイン"));

      webDriver.findElement(By.cssSelector("#email")).sendKeys(kirinUser);
      webDriver.findElement(By.cssSelector("#password")).sendKeys(kirinPassword);
      webDriver.findElement(By.cssSelector("#login_content > ul > li > input")).click();
      log.info("ログイン");

      // 予約時間になるまで待機
      int waitCount = 0;
      LocalDateTime startDateTime = startDateTimeConfig.getStartDateTime();
      while (LocalDateTime.now(ZoneId.of("Asia/Tokyo")).isBefore(startDateTime)) {
        // 100,000,000回ループするごとにログ表示
        if (waitCount % (1000 * 1000 * 100) == 0) {
          log.info("待機中");
        }
        waitCount++;
      }

      log.info("予約開始");

      webDriver.navigate().refresh();

      // 予約ボタンが表示されるまで待機
      webDriverWait.until(webDriver -> webDriver
          .findElement(
              By.cssSelector("#reserve_show_periods_1 > table > tbody > tr.row-available > td:nth-child(6) > a"))
          .isDisplayed());

      webDriver
          .findElement(
              By.cssSelector("#reserve_show_periods_1 > table > tbody > tr.row-available > td:nth-child(6) > a"))
          .click();

      if (Objects.equals(targetName, Name.NAO.getValue())) { // 尚大の予約のとき

        // 予約対象者が表示されるまで待機
        webDriverWait.until(webDriver -> webDriver.findElement(By.cssSelector("#user_id_11830"))
            .isDisplayed());

        // 予約対象者にチェックされていない場合はクリックしてチェックする
        if (webDriver.findElement(By.cssSelector("#user_id_11830")).isSelected() == false) {
          webDriver.findElement(By.cssSelector("#user_id_11830")).click();
        }

        // Todo: 匡平の診察IDが分かり次第更新 もしかしたら二人同時に予約できる？？
        // USERID埋め込むだけでいけるならENUMにIDも持たせてここを共通化する
      } else if (Objects.equals(targetName, Name.KYO.getValue())) { // 匡平の予約の時
        // // 予約対象者が表示されるまで待機
        // webDriverWait.until(webDriver ->
        // webDriver.findElement(By.cssSelector("#user_id_11830"))
        // .isDisplayed());

        // // 予約対象者にチェックされていない場合はクリックしてチェックする
        // if (driver.findElement(By.cssSelector("#user_id_11830")).isSelected() ==
        // false) {
        // driver.findElement(By.cssSelector("#user_id_11830")).click();
        // }
        log.info("{}の予約はまだできないんです。。。", targetName);
        throw new RuntimeException("まだ予約できません。。。");
      } else {
        log.error("{}は不正なユーザーです!!", targetName);
        throw new RuntimeException("不正なユーザーで予約実行");
      }

      // 予約実行
      webDriver.findElement(By.cssSelector("#reserve > div > form > ul > li > input")).click();

      // 予約確認のアラートが表示されるまで待機する
      webDriverWait.until(ExpectedConditions.alertIsPresent());
      webDriver.switchTo().alert().accept();

      // 予約結果が表示されるまで待機
      // Todo: ここの予約結果のXpathが異なってそう 場合によっては結果スルーしてもいいかも
      webDriverWait.until(webDriver -> webDriver
          .findElement(By.cssSelector("#reserve > div > fieldset:nth-child(5) > div:nth-child(5) > div"))
          .isDisplayed());

      // 予約受付番号を取得
      String reservedOrder = webDriver
          .findElement(By.cssSelector("#reserve > div > fieldset:nth-child(5) > div:nth-child(5) > div")).getText();

      return Integer.parseInt(reservedOrder);

    } catch (Exception e) {
      log.error("{}の予約中にエラー発生 原因: {}", targetName, e.toString());
      throw new RuntimeException(e);
    } finally {
      webDriver.quit();
    }

  }
}
