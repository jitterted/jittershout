package com.jitterted.jittershout.adapter.triggering.twitch4j;

import com.github.twitch4j.kraken.domain.KrakenTeam;

public interface TeamFetcher {
  KrakenTeam fetch();
}
