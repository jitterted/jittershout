package com.jitterted.jittershout;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.CommandEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.events.channel.ChannelGoLiveEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class JitterShoutOutBot {

  private static final String CHANNEL_NAME = "jitterted";
  private static final String TEAM_NAME = "livecoders";

  private TwitchChat twitchChat;
  private Shouter shouter;
  private BotCommandHandler botCommandHandler;
  private Twitch4JTwitchTeam twitchTeam;

  public static void main(String[] args) {
    TwitchProperties twitchProperties = TwitchProperties.loadProperties();
    JitterShoutOutBot jitterShoutOutBot = new JitterShoutOutBot();
    jitterShoutOutBot.connect(twitchProperties);
  }

  public void connect(TwitchProperties twitchProperties) {
    TwitchClient twitchClient = createTwitchClient(twitchProperties);

    registerEventHandlers(twitchClient.getEventManager());

    twitchChat = twitchClient.getChat();
    twitchChat.joinChannel(CHANNEL_NAME);
    twitchChat.sendMessage(CHANNEL_NAME, "The JitterChat ShoutBot is here!");

    TwitchChatMessageSender messageSender = new TwitchChatMessageSender(twitchChat, CHANNEL_NAME);

    BotStatus botStatus = BotStatus.builder()
                                   .shoutOutEnabled(true)
                                   .build();

    TeamFetcher teamFetcher = new TwitchTeamFetcher(twitchClient.getKraken(), TEAM_NAME);
    twitchTeam = new Twitch4JTwitchTeam(teamFetcher);

    shouter = new Shouter(messageSender, twitchTeam, botStatus);

    botCommandHandler = new BotCommandHandler(messageSender, botStatus, new DefaultPermissionChecker());
  }

  @NotNull
  private TwitchClient createTwitchClient(TwitchProperties twitchProperties) {
    OAuth2Credential credential = new OAuth2Credential("twitch", twitchProperties.getOAuthToken());

    TwitchClient twitchClient = TwitchClientBuilder.builder()
                                                   .withClientId(twitchProperties.getTwitchClientId())
                                                   .withClientSecret(twitchProperties.getTwitchClientSecret())
                                                   .withEnableChat(true)
                                                   .withEnableHelix(true)
                                                   .withEnableKraken(true)
                                                   .withChatAccount(credential)
                                                   .withCommandTrigger("!")
                                                   .build();

    twitchClient.getClientHelper()
                .enableStreamEventListener(CHANNEL_NAME);
    return twitchClient;
  }

  private void registerEventHandlers(EventManager eventManager) {
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
    twitchTeam.refresh();
    shouter.resetShoutOutTracking();
  }

  private void onCommand(CommandEvent commandEvent) {
    botCommandHandler.handle(commandEvent);
  }

  private void onChannelMessage(ChannelMessageEvent channelMessageEvent) {
    UserId userId = new UserId(channelMessageEvent.getUser().getId());
    shouter.shoutOutTo(userId);
  }

}
