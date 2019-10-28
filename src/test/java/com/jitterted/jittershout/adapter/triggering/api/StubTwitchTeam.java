package com.jitterted.jittershout.adapter.triggering.api;

import com.jitterted.jittershout.domain.TwitchTeam;
import com.jitterted.jittershout.domain.TwitchUser;
import com.jitterted.jittershout.domain.UserId;

class StubTwitchTeam implements TwitchTeam {
  @Override
  public String name() {
    return "some team name";
  }

  @Override
  public boolean isMember(UserId userId) {
    return false;
  }

  @Override
  public void refresh() {
  }

  @Override
  public TwitchUser userById(UserId userId) {
    return null;
  }

  @Override
  public int count() {
    return 17;
  }
}
