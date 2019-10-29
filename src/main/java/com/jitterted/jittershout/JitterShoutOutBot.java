package com.jitterted.jittershout;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.CommandEvent;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.events.channel.ChannelGoLiveEvent;
import com.jitterted.jittershout.adapter.triggering.twitch4j.BotCommandHandler;
import com.jitterted.jittershout.adapter.triggering.twitch4j.DefaultPermissionChecker;
import com.jitterted.jittershout.adapter.triggering.twitch4j.TeamFetcher;
import com.jitterted.jittershout.adapter.triggering.twitch4j.Twitch4JTwitchTeam;
import com.jitterted.jittershout.adapter.triggering.twitch4j.TwitchChatMessageSender;
import com.jitterted.jittershout.adapter.triggering.twitch4j.TwitchProperties;
import com.jitterted.jittershout.adapter.triggering.twitch4j.TwitchTeamFetcher;
import com.jitterted.jittershout.domain.BotStatus;
import com.jitterted.jittershout.domain.DefaultShouter;
import com.jitterted.jittershout.domain.Shouter;
import com.jitterted.jittershout.domain.TwitchTeam;
import com.jitterted.jittershout.domain.UserId;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class JitterShoutOutBot {

  private static final String CHANNEL_NAME = "jitterted";
  private static final String TEAM_NAME = "livecoders";

  private TwitchChat twitchChat;
  private Shouter shouter;
  private BotCommandHandler botCommandHandler;
  private TwitchTeam twitchTeam;
  private TwitchChatMessageSender messageSender;

  public static JitterShoutOutBot create() {
    TwitchProperties twitchProperties = TwitchProperties.loadProperties();
    JitterShoutOutBot jitterShoutOutBot = new JitterShoutOutBot();
    jitterShoutOutBot.connect(twitchProperties);
    return jitterShoutOutBot;
  }

  private void connect(TwitchProperties twitchProperties) {
    TwitchClient twitchClient = createTwitchClient(twitchProperties);

    registerEventHandlers(twitchClient.getEventManager());

    twitchChat = twitchClient.getChat();
    twitchChat.joinChannel(CHANNEL_NAME);
    twitchChat.sendMessage(CHANNEL_NAME, "The JitterChat ShoutBot is here!");

    messageSender = new TwitchChatMessageSender(twitchChat, CHANNEL_NAME);

    BotStatus botStatus = new BotStatus(true);

    TeamFetcher teamFetcher = new TwitchTeamFetcher(twitchClient.getKraken(), TEAM_NAME);
    twitchTeam = new Twitch4JTwitchTeam(teamFetcher);

    shouter = new DefaultShouter(messageSender, twitchTeam, botStatus);

    botCommandHandler = new BotCommandHandler(messageSender, botStatus, new DefaultPermissionChecker(), shouter);
  }

  public TwitchTeam twitchTeam() {
    return twitchTeam;
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
    messageSender.send("GOING LIVE: Refreshing the %s team membership and resetting Shout-Out Tracking".formatted(TEAM_NAME));
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
