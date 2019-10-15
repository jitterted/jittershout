package com.jitterted;

import com.github.twitch4j.kraken.domain.KrakenTeam;
import com.github.twitch4j.kraken.domain.KrakenTeamUser;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Shouter {
  private final MessageSender messageSender;
  private final Map<UserId, KrakenTeamUser> userMap;
  private final BotStatus botStatus;
  private final String teamName;

  public Shouter(MessageSender messageSender, KrakenTeam team, BotStatus botStatus) {
    this.messageSender = messageSender;
    // TODO: when this is fixed in the next release of Twitch4J, we won't need to check for null anymore
    teamName = team.getDisplayName() != null ? team.getDisplayName() : team.getName();
    userMap = team.getUsers().stream()
                  .collect(Collectors.toMap(UserId::from, Function.identity()));
    this.botStatus = botStatus;
    messageSender.send(
        "Our team is '%s' which has %d members."
            .formatted(teamName, userMap.size())
    );
  }

  public void shoutOutTo(UserId id) {
    if (!botStatus.isShoutOutEnabled()) {
      return;
    }
    if (memberOfTeam(id)) {
      sendShoutOut(id);
      acknowledgeUser(id);
    }
  }

  private void acknowledgeUser(UserId id) {
    userMap.remove(id);
  }

  private void sendShoutOut(UserId id) {
    KrakenTeamUser user = userMap.get(id);
    messageSender.send("Hey, it's " + user.getDisplayName()
                           + ", a member of the " + teamName
                           + " team! Check out their stream at " + user.getUrl());
  }

  private boolean memberOfTeam(UserId id) {
    return userMap.containsKey(id);
  }
}
