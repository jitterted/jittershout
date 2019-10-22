package com.jitterted.jittershout;

import com.github.twitch4j.kraken.domain.KrakenTeam;
import com.jitterted.jittershout.adapter.twitch4j.TeamFetcher;

class StubTeamFetcher implements TeamFetcher {

  private final KrakenTeam krakenTeam;

  public StubTeamFetcher(KrakenTeam krakenTeam) {
    this.krakenTeam = krakenTeam;
  }

  @Override
  public KrakenTeam fetch() {
    return krakenTeam;
  }
}
