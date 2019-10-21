package com.jitterted.jittershout;

import com.github.twitch4j.kraken.TwitchKraken;
import com.github.twitch4j.kraken.domain.KrakenTeam;

public class TwitchTeamFetcher implements TeamFetcher {
  private final TwitchKraken twitchKraken;
  private final String teamName;

  public TwitchTeamFetcher(TwitchKraken twitchKraken, String teamName) {
    this.twitchKraken = twitchKraken;
    this.teamName = teamName;
  }

  @Override
  public KrakenTeam fetch() {
    return twitchKraken.getTeamByName(teamName).execute();
  }
}
