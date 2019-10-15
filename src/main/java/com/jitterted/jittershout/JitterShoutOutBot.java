package com.jitterted.jittershout;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.CommandEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.kraken.TwitchKraken;
import com.github.twitch4j.kraken.TwitchKrakenBuilder;
import com.github.twitch4j.kraken.domain.KrakenTeam;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class JitterShoutOutBot {

  private static final String TWITCH_API_OAUTH_ACCESS_TOKEN_PROPERTY_KEY = "TWITCH_OAUTH_TOKEN";
  private static final String TWITCH_API_CLIENT_ID_PROPERTY_KEY = "TWITCH_API_CLIENT_ID";
  private static final String TWITCH_API_CLIENT_SECRET_PROPERTY_KEY = "TWITCH_API_CLIENT_SECRET";
  private static final String TWITCH_BOT_NICKNAME = "TWITCH_BOT_NICKNAME";

  private static final String CHANNEL_NAME = "jitterted";
  private static final String TEAM_NAME = "livecoders";

  private TwitchChat twitchChat;
  private Shouter shouter;
  private BotCommandHandler botCommandHandler;

  public static void main(String[] args) {
    TwitchProperties twitchProperties = loadProperties();
    JitterShoutOutBot jitterShoutOutBot = new JitterShoutOutBot();
    jitterShoutOutBot.connect(twitchProperties);
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

    TwitchKraken twitchKraken = TwitchKrakenBuilder.builder()
                                                   .withClientId(twitchProperties.getTwitchClientId())
                                                   .withClientSecret(twitchProperties.getTwitchClientSecret())
                                                   .withEventManager(twitchClient.getEventManager())
                                                   .build();
    KrakenTeam krakenTeam = fetchTeam(twitchKraken);

    twitchChat = twitchClient.getChat();
    joinChat(twitchChat);

    BotStatus botStatus = BotStatus.builder()
                                   .shoutOutEnabled(true)
                                   .build();

    TwitchChatMessageSender messageSender = new TwitchChatMessageSender(twitchChat, CHANNEL_NAME);
    shouter = new Shouter(messageSender, krakenTeam, botStatus);

    botCommandHandler = new BotCommandHandler(messageSender, krakenTeam, botStatus, new DefaultPermissionChecker());
  }

  private KrakenTeam fetchTeam(TwitchKraken kraken) {
    return kraken.getTeamByName(TEAM_NAME).execute();
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
    botCommandHandler.handle(commandEvent);
  }

  private void onChannelMessage(ChannelMessageEvent channelMessageEvent) {
    UserId userId = new UserId(channelMessageEvent.getUser().getId());
    log.info(channelMessageEvent.getPermissions().toString());
    shouter.shoutOutTo(userId);
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
