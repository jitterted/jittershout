package com.jitterted;

import com.github.twitch4j.kraken.domain.KrakenTeamUser;
import lombok.Data;

@Data
public class UserId {
  private final String id;

  public static UserId from(long id) {
    return new UserId(String.valueOf(id));
  }

  public static UserId from(KrakenTeamUser krakenTeamUser) {
    return from(krakenTeamUser.getId());
  }

}
