package com.jitterted.jittershout;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.CommandEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.events.channel.ChannelGoLiveEvent;
import com.github.twitch4j.kraken.TwitchKraken;
import com.github.twitch4j.kraken.TwitchKrakenBuilder;
import com.github.twitch4j.kraken.domain.KrakenTeam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JitterShoutOutBot {

  private static final String CHANNEL_NAME = "jitterted";
  private static final String TEAM_NAME = "livecoders";

  private TwitchChat twitchChat;
  private Shouter shouter;
  private BotCommandHandler botCommandHandler;

  public static void main(String[] args) {
    TwitchProperties twitchProperties = TwitchProperties.loadProperties();
    JitterShoutOutBot jitterShoutOutBot = new JitterShoutOutBot();
    jitterShoutOutBot.connect(twitchProperties);
  }

  public void connect(TwitchProperties twitchProperties) {
    TwitchClient twitchClient = createTwitchClient(twitchProperties);

    twitchClient.getClientHelper()
                .enableStreamEventListener(CHANNEL_NAME);

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

    EventManager eventManager = chat.getEventManager();

    eventManager
        .onEvent(ChannelMessageEvent.class)
        .subscribe(this::onChannelMessage);

    eventManager
        .onEvent(CommandEvent.class)
        .subscribe(this::onCommand);

    eventManager
        .onEvent(ChannelGoLiveEvent.class)
        .subscribe(this::onChannelGoLive);
  }

  private void onChannelGoLive(ChannelGoLiveEvent channelGoLiveEvent) {
    // integrate with reload of team list
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
                              .withEnableHelix(true)
                              .withChatAccount(credential)
                              .withCommandTrigger("!")
                              .build();
  }

}
