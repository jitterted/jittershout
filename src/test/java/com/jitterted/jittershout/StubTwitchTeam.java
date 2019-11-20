package com.jitterted.jittershout;

import com.jitterted.jittershout.domain.TwitchTeam;
import com.jitterted.jittershout.domain.TwitchUser;
import com.jitterted.jittershout.domain.UserId;

public class StubTwitchTeam implements TwitchTeam {
  private final boolean defaultIsMember;

  public StubTwitchTeam(boolean defaultIsMember) {
    this.defaultIsMember = defaultIsMember;
  }

  public StubTwitchTeam() {
    this(true);
  }

  @Override
  public String name() {
    return "stub";
  }

  @Override
  public boolean isMember(UserId userId) {
    return defaultIsMember;
  }

  @Override
  public void refresh() {
  }

  @Override
  public TwitchUser userById(UserId userId) {
    return new TwitchUser(userId, "JitterTed", "https://twitch.tv/jitterted");
  }

  @Override
  public int count() {
    return 17;
  }
}
