package com.kirin.reservation.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDriverConfig {

  @Value("${selenium.host}")
  private String seleniumHost;

  // seleniumに渡すオプションを定義
  private static final ChromeOptions chromeOptions = new ChromeOptions()
      .addArguments("--no-sandbox", "--disable-gpu", "--disable-dev-shm-usage")
      .setHeadless(true);

  private static final Duration DURATION_TIMEOUT_SECONDS = Duration.ofSeconds(30);

  /**
   * chromeを操作するWebDriverを取得する
   *
   * @return WebDriverインスタンス
   */
  public RemoteWebDriver getWebDriver() {
    try {
      RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(seleniumHost), chromeOptions);
      remoteWebDriver.manage().timeouts().implicitlyWait(DURATION_TIMEOUT_SECONDS);
      return remoteWebDriver;
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
