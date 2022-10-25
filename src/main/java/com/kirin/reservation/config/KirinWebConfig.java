package com.kirin.reservation.config;

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

  @Value("${kirin.target-id}")
  private String targetUserId;

  private static final String TITLE_LOGIN = "ログイン";
  private static final String SELECTOR_EMAIL = "#email";
  private static final String SELECTOR_PASSWORD = "#password";
  private static final String SELECTOR_LOGIN = "#login_content > ul > li > input";
  private static final String SELECTOR_RESERVE =
      "#reserve_show_periods_1 > table > tbody > tr.row-available > td:nth-child(6) > a";
  private static final String SELECTOR_USER_ID = "#user_id_%s";
  
  private static final String SELECTOR_EXECUTE = "#reserve > div > form > ul > li > input";

  public String loginTitle() {
    return TITLE_LOGIN;
  }

  public By emailSelector() {
    return By.cssSelector(SELECTOR_EMAIL);
  }

  public By passwordSelector() {
    return By.cssSelector(SELECTOR_PASSWORD);
  }

  public By loginSelector() {
    return By.cssSelector(SELECTOR_LOGIN);
  }

  public By reserveSelector() {
    return By.cssSelector(SELECTOR_RESERVE);
  }

  public By userIdSelector() {
    return By.cssSelector(String.format(SELECTOR_USER_ID, this.targetUserId));
  }

  public By executeSelector() {
    return By.cssSelector(SELECTOR_EXECUTE);
  }

  public String url() {
    return this.kirinUrl;
  }

  public String password() {
    return this.kirinPassword;
  }

  public String user() {
    return this.kirinUser;
  }


}
