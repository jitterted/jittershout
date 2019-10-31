package com.jitterted.jittershout.adapter.triggering.api;

import com.jitterted.jittershout.domain.BotStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BotControllerTest {

  @Test
  public void whenShoutOutIsOnBotInfoReturnsActive() throws Exception {
    BotStatus botStatus = new BotStatus(true);
    BotController botController = new BotController(botStatus);

    BotInfoDto botInfoDto = botController.botInfo();

    assertThat(botInfoDto.isShoutOutActive())
        .isEqualTo(true);
  }

  @Test
  public void whenShoutOutIsOffBotInfoReturnsInactive() throws Exception {
    BotStatus botStatus = new BotStatus(false);
    BotController botController = new BotController(botStatus);

    BotInfoDto botInfoDto = botController.botInfo();

    assertThat(botInfoDto.isShoutOutActive())
        .isEqualTo(false);
  }
}
