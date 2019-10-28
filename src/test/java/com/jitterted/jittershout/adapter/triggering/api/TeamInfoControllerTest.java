package com.jitterted.jittershout.adapter.triggering.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TeamInfoControllerTest {

  @Test
  public void getReturnsTeamInformation() throws Exception {
    TeamInfoController teamInfoController = new TeamInfoController(new StubTwitchTeam());

    TeamInfoDto teamInfoDto = teamInfoController.teamInfo();

    assertThat(teamInfoDto.getName())
        .isEqualTo("some team name");
    assertThat(teamInfoDto.getCount())
        .isEqualTo("17");
  }

}