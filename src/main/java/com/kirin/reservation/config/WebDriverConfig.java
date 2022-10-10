package com.kirin.reservation.config;

import java.net.MalformedURLException;
import java.net.URL;

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
      // .addArguments("--no-sandbox", "--disable-dev-shm-usage")
      .addArguments("--no-sandbox", "--disable-gpu", "--disable-dev-shm-usage")
      .setHeadless(false);

  /**
   * chromeを操作するWebdriverを取得する
   * 
   * @return Webドライバ
   * @throws MalformedURLException
   */
  public RemoteWebDriver getWebDriver() throws MalformedURLException {
    return new RemoteWebDriver(new URL(seleniumHost), chromeOptions);
  }

}
