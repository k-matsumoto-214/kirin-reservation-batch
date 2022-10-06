package com.kirin.reservation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingClient;

@Component
public class LineMessageClientConfig {

  @Value("${line.bot.channel-token}")
  private String channelToken;

  @Bean
  public LineMessagingClient lineMessageClient() {
    return LineMessagingClient
        .builder(channelToken)
        .build();
  }
}
