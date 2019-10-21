package com.jitterted.jittershout;

import java.util.HashSet;
import java.util.Set;

public class DefaultShouter implements Shouter {
  private final MessageSender messageSender;
  private final TwitchTeam twitchTeam;
  private final BotStatus botStatus;
  private final Set<UserId> shoutedOutAtUsers = new HashSet<>();

  public DefaultShouter(MessageSender messageSender, TwitchTeam twitchTeam, BotStatus botStatus) {
    this.messageSender = messageSender;
    this.twitchTeam = twitchTeam;
    this.botStatus = botStatus;
  }

  @Override
  public void shoutOutTo(UserId id) {
    if (!botStatus.isShoutOutEnabled()) {
      return;
    }
    if (twitchTeam.isMember(id)) {
      sendShoutOut(id);
    }
  }

  @Override
  public void resetShoutOutTracking() {
    shoutedOutAtUsers.clear();
  }

  private void sendShoutOut(UserId id) {
    if (alreadyShoutedOutTo(id)) {
      return;
    }
    TwitchUser twitchUser = twitchTeam.userById(id);
    messageSender.send("Hey, it's "
                           + twitchUser.name()
                           + ", a member of the " + twitchTeam.name()
                           + " team! Check out their stream at "
                           + twitchUser.url()
    );
    markUserAsShoutedOutTo(id);
  }

  private void markUserAsShoutedOutTo(UserId id) {
    shoutedOutAtUsers.add(id);
  }

  private boolean alreadyShoutedOutTo(UserId id) {
    return shoutedOutAtUsers.contains(id);
  }
}
