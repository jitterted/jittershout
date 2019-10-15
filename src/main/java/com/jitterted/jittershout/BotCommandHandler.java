package com.jitterted.jittershout;

import com.github.twitch4j.chat.events.CommandEvent;
import com.github.twitch4j.kraken.domain.KrakenTeam;
import org.jetbrains.annotations.NotNull;

public class BotCommandHandler {

  private final MessageSender messageSender;
  private final KrakenTeam krakenTeam;
  private final BotStatus botStatus;
  private final PermissionChecker permissionChecker;


  public BotCommandHandler(MessageSender messageSender, KrakenTeam krakenTeam, BotStatus botStatus) {
    this(messageSender, krakenTeam, botStatus, new AlwaysAllowedPermissionChecker());
  }

  public BotCommandHandler(MessageSender messageSender, KrakenTeam krakenTeam, BotStatus botStatus, PermissionChecker permissionChecker) {
    this.messageSender = messageSender;
    this.krakenTeam = krakenTeam;
    this.botStatus = botStatus;
    this.permissionChecker = permissionChecker;
  }

  public void handle(CommandEvent commandEvent) {
    if (!permissionChecker.allows(commandEvent.getPermissions())) {
      return;
    }

    String[] tokens = commandEvent.getCommand().split(" ");
    if (validShoutOutBotCommand(tokens)) {
      handleShoutOutBotCommand(tokens[1]);
    }
  }

  private boolean validShoutOutBotCommand(String[] tokens) {
    return isShoutOutCommand(tokens[0]) && hasSubCommand(tokens);
  }

  private boolean isShoutOutCommand(String token) {
    return token.equals("sob");
  }

  private boolean hasSubCommand(String[] tokens) {
    return tokens.length > 1;
  }

  private void handleShoutOutBotCommand(String subcommand) {
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
