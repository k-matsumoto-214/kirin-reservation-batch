package com.kirin.reservation.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDriverConfig {

  @Value("${selenium.host}")
  private String seleniumHost;

  // seleniumに渡すオプションを定義
  private static final ChromeOptions chromeOptions = new ChromeOptions()
      // .addArguments("--no-sandbox", "--disable-dev-shm-usage")
      .addArguments("--no-sandbox", "--disable-gpu", "--disable-dev-shm-usage")
      .setHeadless(false);

  private static final Duration DURATION_TIMEOUT_SECONDS = Duration.ofSeconds(10);
  private static final Duration DURATION_SLEEP_MILLIS = Duration.ofMillis(30);

  /**
   * chromeを操作するWebDriverを取得する
   *
   * @return WebDriverインスタンス
   */
  public RemoteWebDriver getWebDriver() {
    try {
      return new RemoteWebDriver(new URL(seleniumHost), chromeOptions);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * chromeを操作するWebdriverWaitを取得する
   *
   * @param webDriver 利用するWebDriver
   * @return WebDriverWaitインスタンス
   */
  public WebDriverWait getWebDriverWait(WebDriver webDriver) {
    return new WebDriverWait(webDriver, DURATION_TIMEOUT_SECONDS, DURATION_SLEEP_MILLIS);
  }

  /**
   * chromeを操作するWebdriverWaitを取得する
   *
   * @param webDriver 利用するWebDriver
   * @return WebDriverWaitインスタンス
   */
  public WebDriverWait getWebDriverWait(WebDriver webDriver) {
    return new WebDriverWait(webDriver, DURATION_TIMEOUT_SECONDS, DURATION_SLEEP_MILLIS);
  }

}
