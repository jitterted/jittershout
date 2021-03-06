package com.jitterted.jittershout.adapter.triggering.api;

import com.jitterted.jittershout.domain.TwitchTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TeamInfoController {

  private final TwitchTeam twitchTeam;

  @Autowired
  public TeamInfoController(TwitchTeam twitchTeam) {
    this.twitchTeam = twitchTeam;
  }

  @GetMapping("/teaminfo")
  public TeamInfoDto teamInfo() {
    return new TeamInfoDto(twitchTeam.name(), String.valueOf(twitchTeam.count()));
  }

  @PostMapping("/refresh-team")
  public ResponseEntity<String> refreshTeam() {
    twitchTeam.refresh();
    return ResponseEntity.ok("refreshed");
  }
}
