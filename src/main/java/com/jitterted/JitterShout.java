package com.jitterted;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.CommandEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class JitterShout {

  private static final String TWITCH_API_OAUTH_ACCESS_TOKEN_PROPERTY_KEY = "TWITCH_OAUTH_TOKEN";
  private static final String TWITCH_API_CLIENT_ID_PROPERTY_KEY = "TWITCH_API_CLIENT_ID";
  private static final String TWITCH_API_CLIENT_SECRET_PROPERTY_KEY = "TWITCH_API_CLIENT_SECRET";
  private static final String TWITCH_BOT_NICKNAME = "TWITCH_BOT_NICKNAME";

  private static final String CHANNEL_NAME = "jitterted";
  private static final String TEAM_NAME = "livecoders";

  private Map<Long, TwitchUser> twitchUserMap = new HashMap<>();
  private TwitchChat twitchChat;
  private Shouter shouter;

  public static void main(String[] args) {
    TwitchProperties twitchProperties = loadProperties();
    JitterShout jitterShout = new JitterShout();
    jitterShout.connect(twitchProperties);
  }

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

  public void connect(TwitchProperties twitchProperties) {
    TwitchClient twitchClient = createTwitchClient(twitchProperties);

    TwitchTeam twitchTeam = fetchTeam((JitterTwitchKraken) JitterTwitchKrakenBuilder
        .builder()
        .withClientId(twitchProperties.getTwitchClientId())
        .withClientSecret(twitchProperties.getTwitchClientSecret())
        .withEventManager(twitchClient.getEventManager())
        .build());

    twitchChat = twitchClient.getChat();
    joinChat(twitchChat);

    shouter = new Shouter(new TwitchChatMessageSender(twitchChat, CHANNEL_NAME), twitchTeam);

  }

  private TwitchTeam fetchTeam(JitterTwitchKraken kraken) {
    return kraken.getTeamWithUsersByName(TEAM_NAME).execute();
  }

  private void joinChat(TwitchChat chat) {
    chat.joinChannel(CHANNEL_NAME);
    chat.sendMessage(CHANNEL_NAME, "The JitterChat ShoutBot is here!");

    EventManager chatEventManager = chat.getEventManager();

    chatEventManager
        .onEvent(ChannelMessageEvent.class)
        .subscribe(this::onChannelMessage);

    chatEventManager
        .onEvent(CommandEvent.class)
        .subscribe(this::onCommand);
  }

  private void onCommand(CommandEvent commandEvent) {

  }

  private void onChannelMessage(ChannelMessageEvent channelMessageEvent) {
    long userId = channelMessageEvent.getUser().getId();
    shouter.shoutTo(userId);
  }

  private TwitchClient createTwitchClient(TwitchProperties twitchProperties) {
    OAuth2Credential credential = new OAuth2Credential("twitch", twitchProperties.getOAuthToken());

    return TwitchClientBuilder.builder()
                              .withClientId(twitchProperties.getTwitchClientId())
                              .withClientSecret(twitchProperties.getTwitchClientSecret())
                              .withEnableChat(true)
                              .withChatAccount(credential)
                              .withCommandTrigger("!")
                              .build();
  }

}
