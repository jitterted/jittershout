package com.jitterted.jittershout.adapter.triggering.api;

import com.jitterted.jittershout.StubTwitchTeam;
import com.jitterted.jittershout.domain.TwitchTeam;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class TeamInfoControllerTest {

  @Test
  public void getReturnsTeamInformation() throws Exception {
    TeamInfoController teamInfoController = new TeamInfoController(new StubTwitchTeam());

    TeamInfoDto teamInfoDto = teamInfoController.teamInfo();

    assertThat(teamInfoDto.getName())
        .isEqualTo("stub");
    assertThat(teamInfoDto.getCount())
        .isEqualTo("17");
  }

  @Test
  public void postToRefreshTeamTriggersTeamRefresh() throws Exception {
    TwitchTeam twitchTeamSpy = Mockito.mock(TwitchTeam.class);
    TeamInfoController teamInfoController = new TeamInfoController(twitchTeamSpy);

    teamInfoController.refreshTeam();

    Mockito.verify(twitchTeamSpy).refresh();
  }

}