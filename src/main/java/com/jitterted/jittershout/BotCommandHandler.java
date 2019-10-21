package com.jitterted.jittershout;

import com.github.twitch4j.chat.events.CommandEvent;
import org.jetbrains.annotations.NotNull;

public class BotCommandHandler {

  private final MessageSender messageSender;
  private final BotStatus botStatus;
  private final PermissionChecker permissionChecker;
  private final Shouter shouter;

  public BotCommandHandler(MessageSender messageSender,
                           BotStatus botStatus,
                           PermissionChecker permissionChecker,
                           Shouter shouter) {
    this.messageSender = messageSender;
    this.botStatus = botStatus;
    this.permissionChecker = permissionChecker;
    this.shouter = shouter;
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
      case "reset" -> shouter.resetShoutOutTracking();
    }
  }

  private void changeShoutOutTo(boolean enabled) {
    botStatus.setShoutOutEnabled(enabled);
    messageSender.send("Shout-out is now %s".formatted(shoutOutStatusAsText()));
  }

  private String botStatusMessage() {
    return "Shout-out is %s.".formatted(shoutOutStatusAsText());
  }

  @NotNull
  private String shoutOutStatusAsText() {
    return botStatus.isShoutOutEnabled() ? "on" : "off";
  }

}
