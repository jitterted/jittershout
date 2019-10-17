package com.jitterted.jittershout;

import com.github.twitch4j.kraken.domain.KrakenTeam;
import com.github.twitch4j.kraken.domain.KrakenTeamUser;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TwitchTeamTest {

  @Test
  public void teamNameIsShortNameWhenThereIsNoDisplayName() throws Exception {
    KrakenTeam krakenTeam = new KrakenTeam();
    krakenTeam.setName("shortname");
    TwitchTeam twitchTeam = new TwitchTeam(new StubTeamFetcher(krakenTeam));

    assertThat(twitchTeam.name())
        .isEqualTo("shortname");
  }

  @Test
  public void teamNameIsDisplayNameWhenDisplayNameExists() throws Exception {
    KrakenTeam krakenTeam = new KrakenTeam();
    krakenTeam.setName("shortname");
    krakenTeam.setDisplayName("The Display Name");
    TwitchTeam twitchTeam = new TwitchTeam(new StubTeamFetcher(krakenTeam));

    assertThat(twitchTeam.name())
        .isEqualTo("The Display Name");
  }

  @Test
  public void emptyTeamMeansNoUserIsMemberOfTeam() throws Exception {
    KrakenTeam krakenTeam = new KrakenTeam();
    krakenTeam.setUsers(Collections.emptyList());
    TwitchTeam twitchTeam = new TwitchTeam(new StubTeamFetcher(krakenTeam));

    assertThat(twitchTeam.isMember(UserId.from(1L)))
        .isFalse();
  }

  @Test
  public void teamWithUserIsMemberOfTeam() throws Exception {
    KrakenTeam krakenTeam = new KrakenTeam();
    KrakenTeamUser teamUser = new KrakenTeamUser();
    teamUser.setId(31L);
    krakenTeam.setUsers(List.of(teamUser));
    TwitchTeam twitchTeam = new TwitchTeam(new StubTeamFetcher(krakenTeam));

    assertThat(twitchTeam.isMember(UserId.from(31L)))
        .isTrue();
  }

  static class StubTeamFetcher implements TeamFetcher {

    private final KrakenTeam krakenTeam;

    public StubTeamFetcher(KrakenTeam krakenTeam) {
      this.krakenTeam = krakenTeam;
    }

    @Override
    public KrakenTeam fetch() {
      return krakenTeam;
    }
  }
}
