package com.kirin.reservation.service;

import com.kirin.reservation.config.KirinWebConfig;
import com.kirin.reservation.config.TimeConfig;
import com.kirin.reservation.config.WebDriverConfig;
import com.kirin.reservation.model.ReservationDate;
import com.kirin.reservation.model.ReservationTime;
import com.kirin.reservation.repository.database.ReservationDateRepository;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationDateRepository reservationDateRepository;

  private final WebDriverConfig webDriverConfig;

  private final TimeConfig timeConfig;

  private final KirinWebConfig webConfig;

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
    RemoteWebDriver webDriver = webDriverConfig.getWebDriver();

    try {
      log.info("{}の予約を開始", targetName);

      WebDriverWait webDriverWait = webDriverConfig.getWebDriverWait(webDriver);
      webDriver.get(webConfig.url());

      // ログイン画面表示待機
      webDriverWait.until(driver -> driver.getTitle().startsWith(webConfig.loginTitle()));

      // email入力
      webDriver.findElement(webConfig.emailSelector()).sendKeys(webConfig.user());

      // password入力
      webDriver.findElement(webConfig.passwordSelector()).sendKeys(webConfig.password());

      // ログイン実行
      webDriver.findElement(webConfig.loginSelector()).click();

      log.info("ログイン");

      // 予約開始時間まで待機
      timeConfig.until(reservationTime);

      log.info("予約開始");

      // 予約開始時間になったら画面更新
      webDriver.navigate().refresh();

      // 予約ボタンが表示されるまで待機
      webDriverWait.until(driver ->
          driver.findElement(webConfig.reserveSelector())
              .isDisplayed());

      // 予約ボタンクリック
      webDriver.findElement(webConfig.reserveSelector()).click();

      // 予約対象者が表示されるまで待機
      webDriverWait.until(driver -> driver
          .findElement(webConfig.userIdSelector())
          .isEnabled());

      // スクリーンショットを取得し、ファイルを退避する。 ← ← ← ここを追加。
      File file = webDriver.getScreenshotAs(OutputType.FILE);
      Files.copy(file.toPath(), new File(
          "/home/keismats/logs/kirin/reservation-batch-nao/screenshot/before-"
              + file.getName()).toPath());

      // 予約対象者にチェックする
      boolean isChecked = webDriver.findElement(webConfig.userIdSelector()).isSelected();

      if (isChecked) {
        log.info("チェック済み");
      }

      if (!isChecked) {
        log.info("チェックされていないので対象者をチェック");
        webDriver.findElement(webConfig.userIdSelector()).click();
      }

      // スクリーンショットを取得し、ファイルを退避する。 ← ← ← ここを追加。
      File file2 = webDriver.getScreenshotAs(OutputType.FILE);
      Files.copy(file2.toPath(), new File(
          "/home/keismats/logs/kirin/reservation-batch-nao/screenshot/after-"
              + file.getName()).toPath());

      // 予約実行
      webDriver.findElement(webConfig.executeSelector()).click();

      // 予約確認のアラートが表示されるまで待機する
      webDriverWait.until(ExpectedConditions.alertIsPresent());

      // アラートの確認を受け入れる
      webDriver.switchTo().alert().accept();

      return true;

    } catch (Exception e) {

      log.error("{}の予約中にエラー発生 原因: {}", targetName, e.toString());

      return false;

    } finally {
      webDriver.quit();
    }
  }
}
