package com.jitterted;

import com.github.twitch4j.chat.events.CommandEvent;
import com.github.twitch4j.kraken.domain.KrakenTeam;
import org.jetbrains.annotations.NotNull;

public class BotCommandHandler {

  private final MessageSender messageSender;
  private final KrakenTeam krakenTeam;
  private final BotStatus botStatus;

  public BotCommandHandler(MessageSender messageSender, KrakenTeam krakenTeam, BotStatus botStatus) {
    this.messageSender = messageSender;
    this.krakenTeam = krakenTeam;
    this.botStatus = botStatus;
  }

  public void handle(CommandEvent commandEvent) {
    String[] tokens = commandEvent.getCommand().split(" ");
    if (tokens.length > 1) {
      handleSubcommand(tokens[1]);
    }
  }

  private void handleSubcommand(String subcommand) {
    switch (subcommand) {
      case "status" -> messageSender.send(botStatusMessage());
      case "off" -> changeShoutOutTo(false);
      case "on" -> changeShoutOutTo(true);
    }
  }

  private void changeShoutOutTo(boolean enabled) {
    botStatus.setShoutOutEnabled(enabled);
    messageSender.send("Shout-out is now %s".formatted(shoutOutStatusAsText()));
  }

  private String botStatusMessage() {
    return ("Shout-out is %s, for the %d team members of '%s'.")
        .formatted(shoutOutStatusAsText(),
                   krakenTeam.getUsers().size(),
                   krakenTeam.getName());
  }

  @NotNull
  private String shoutOutStatusAsText() {
    return botStatus.isShoutOutEnabled() ? "on" : "off";
  }
}
