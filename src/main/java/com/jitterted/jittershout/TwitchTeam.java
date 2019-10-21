package com.jitterted.jittershout;

public interface TwitchTeam {
  String name();

  boolean isMember(UserId userId);

  void refresh();

  TwitchUser userById(UserId userId);
}
