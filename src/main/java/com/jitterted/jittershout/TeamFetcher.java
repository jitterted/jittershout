package com.jitterted.jittershout;

import com.github.twitch4j.kraken.domain.KrakenTeam;

public interface TeamFetcher {
  KrakenTeam fetch();
}
