package com.jitterted.jittershout;

import lombok.Data;

@Data
public class TwitchProperties {
  private final String oAuthToken;
  private final String twitchClientId;
  private final String twitchClientSecret;
  private final String twitchBotNickname;
}
