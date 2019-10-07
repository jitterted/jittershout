package com.jitterted;

import com.github.twitch4j.kraken.TwitchKraken;
import com.netflix.hystrix.HystrixCommand;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface JitterTwitchKraken extends TwitchKraken {
  @RequestLine("GET /teams/{name}")
  @Headers("Accept: application/vnd.twitchtv.v5+json")
  HystrixCommand<TwitchTeam> getTeamWithUsersByName(
      @Param("name") String name
  );

}
