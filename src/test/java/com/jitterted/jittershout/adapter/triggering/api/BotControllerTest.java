package com.jitterted.jittershout.adapter.triggering.api;

import org.junit.jupiter.api.Test;

public class BotControllerTest {

  @Test
  public void turnOffShoutOutViaPostMeansNoShoutOutToTeamMembers() throws Exception {
    BotController botController = new BotController(null);

    botController.updateBotState(new BotInfoDto(false))
  }
}
