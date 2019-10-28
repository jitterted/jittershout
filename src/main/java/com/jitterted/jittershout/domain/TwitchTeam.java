package com.jitterted.jittershout.domain;

public interface TwitchTeam {
  String name();

  boolean isMember(UserId userId);

  void refresh();

  TwitchUser userById(UserId userId);

  int count();
}
