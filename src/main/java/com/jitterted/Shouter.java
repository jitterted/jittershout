package com.jitterted;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Shouter {
  private final MessageSender messageSender;
  private final Map<Long, TwitchUser> userMap;
  private final String teamName;

  public Shouter(MessageSender messageSender, TwitchTeam team) {
    this.messageSender = messageSender;
    teamName = team.getDisplayName();
    userMap = team.getUsers().stream()
                  .collect(Collectors.toMap(TwitchUser::getId, Function.identity()));
  }

  public void shoutTo(long id) {
    if (userMap.containsKey(id)) {
      TwitchUser user = userMap.get(id);
      messageSender.send("Hey, it's " + user.getDisplayName()
                             + ", a member of the " + teamName
                             + " team! Check out their stream at " + user.getUrl());
      userMap.remove(id);
    }
  }
}
