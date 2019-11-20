package com.jitterted.jittershout.adapter.triggering.api;

import com.jitterted.jittershout.domain.Shouter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {
  private final Shouter shouter;

  @Autowired
  public BotController(Shouter shouter) {
    this.shouter = shouter;
  }

  @GetMapping("/api/botinfo")
  public BotInfoDto botInfo() {
    return createBotInfoDto();
  }

  @PostMapping("/api/botinfo")
  public BotInfoDto updateBotState(@RequestBody BotInfoDto botInfoDto) {
    shouter.changeShoutOutActiveTo(botInfoDto.isShoutOutActive());
    return createBotInfoDto();
  }

  @NotNull
  private BotInfoDto createBotInfoDto() {
    return new BotInfoDto(shouter.isShoutOutActive(), 3);
  }
}
