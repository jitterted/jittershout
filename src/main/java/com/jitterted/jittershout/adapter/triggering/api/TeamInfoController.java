package com.jitterted.jittershout.adapter.triggering.api;

import com.jitterted.jittershout.domain.TwitchTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamInfoController {

  private final TwitchTeam twitchTeam;

  @Autowired
  public TeamInfoController(TwitchTeam twitchTeam) {
    this.twitchTeam = twitchTeam;
  }

  @GetMapping("/api/teaminfo")
  public TeamInfoDto teamInfo() {
    return new TeamInfoDto(twitchTeam.name(), String.valueOf(twitchTeam.count()));
  }

}
