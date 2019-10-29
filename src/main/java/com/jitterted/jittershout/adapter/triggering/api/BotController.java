package com.jitterted.jittershout.adapter.triggering.api;

import com.jitterted.jittershout.domain.BotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {
  private final BotStatus botStatus;

  @Autowired
  public BotController(BotStatus botStatus) {
    this.botStatus = botStatus;
  }

  @GetMapping("/api/botinfo")
  public BotInfoDto botInfo() {
    String shoutOutState = botStatus.isShoutOutEnabled() ? "active" : "inactive";
    return new BotInfoDto(shoutOutState);
  }
}