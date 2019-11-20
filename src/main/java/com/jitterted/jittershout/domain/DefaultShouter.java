package com.jitterted.jittershout.domain;

import java.util.HashSet;
import java.util.Set;

public class DefaultShouter implements Shouter {
  private final MessageSender messageSender;
  private final TwitchTeam twitchTeam;
  private final Set<UserId> shoutedOutAtUsers = new HashSet<>();
  private boolean shoutOutActive = true;

  public DefaultShouter(MessageSender messageSender, TwitchTeam twitchTeam) {
    this.messageSender = messageSender;
    this.twitchTeam = twitchTeam;
  }

  @Override
  public void shoutOutTo(UserId id) {
    if (!shoutOutActive) {
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

  @Override
  public int shoutOutTrackingCount() {
    return shoutedOutAtUsers.size();
  }

  @Override
  public void changeShoutOutActiveTo(boolean isActive) {
    shoutOutActive = isActive;
  }

  @Override
  public boolean isShoutOutActive() {
    return shoutOutActive;
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
