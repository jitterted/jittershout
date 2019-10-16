package com.jitterted.jittershout;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Data
@Slf4j
public class TwitchProperties {
  private static final String TWITCH_API_OAUTH_ACCESS_TOKEN_PROPERTY_KEY = "TWITCH_OAUTH_TOKEN";
  private static final String TWITCH_API_CLIENT_ID_PROPERTY_KEY = "TWITCH_API_CLIENT_ID";
  private static final String TWITCH_API_CLIENT_SECRET_PROPERTY_KEY = "TWITCH_API_CLIENT_SECRET";
  private static final String TWITCH_BOT_NICKNAME = "TWITCH_BOT_NICKNAME";

  private final String oAuthToken;
  private final String twitchClientId;
  private final String twitchClientSecret;
  private final String twitchBotNickname;

  public static TwitchProperties loadProperties() {
    Properties properties = new Properties();
    try {
      properties.load(new FileReader("/Users/ted/.twitch.properties"));
      String oAuthToken = getValidPropertyFrom(properties, TWITCH_API_OAUTH_ACCESS_TOKEN_PROPERTY_KEY);
      String twitchClientId = getValidPropertyFrom(properties, TWITCH_API_CLIENT_ID_PROPERTY_KEY);
      String twitchClientSecret = getValidPropertyFrom(properties, TWITCH_API_CLIENT_SECRET_PROPERTY_KEY);
      String twitchBotNickname = getValidPropertyFrom(properties, TWITCH_BOT_NICKNAME);
      return new TwitchProperties(oAuthToken, twitchClientId, twitchClientSecret, twitchBotNickname);
    } catch (IOException e) {
      log.error("Could not read .twitch.properties file", e);
      System.exit(-1);
    }
    throw new IllegalStateException();
  }

  private static String getValidPropertyFrom(Properties properties, String key) throws IOException {
    String property = properties.getProperty(key);
    if (property == null) {
      throw new IOException("Could not find property key '" + key + "' in properties file.");
    }
    return property;
  }
}
