package com.kirin.reservation.config;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfig {

  private static ZoneId zoneId = ZoneId.of("Asia/Tokyo");

  @Bean
  public Clock defaultClock() {
    return Clock.system(zoneId);
  }
}
