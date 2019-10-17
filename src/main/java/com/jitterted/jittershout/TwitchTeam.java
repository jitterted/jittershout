package com.jitterted.jittershout;

import com.github.twitch4j.kraken.domain.KrakenTeam;

public class TwitchTeam {
  private String teamName;

  public TwitchTeam(TeamFetcher teamFetcher) {
    KrakenTeam krakenTeam = teamFetcher.fetch();
    teamName = krakenTeam.getDisplayName() != null ? krakenTeam.getDisplayName() : krakenTeam.getName();
  }

  public String name() {
    return teamName;
  }

  public boolean isMember(UserId userId) {
    return false;
  }
}
