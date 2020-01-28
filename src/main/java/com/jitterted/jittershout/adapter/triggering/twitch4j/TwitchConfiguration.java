package com.jitterted.jittershout.adapter.triggering.twitch4j;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "twitch")
public class TwitchConfiguration {
  private String oAuthToken;
  private String clientId;
  private String clientSecret;
  private String botNickname;

  private String channelName;
  private String teamName;

  public String getoAuthToken() {
    return oAuthToken;
  }

  public void setoAuthToken(String oAuthToken) {
    this.oAuthToken = oAuthToken;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getBotNickname() {
    return botNickname;
  }

  public void setBotNickname(String botNickname) {
    this.botNickname = botNickname;
  }

  public String getChannelName() {
    return channelName;
  }

  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }
}
