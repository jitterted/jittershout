package com.jitterted.jittershout;

import com.github.twitch4j.kraken.domain.KrakenTeam;
import com.github.twitch4j.kraken.domain.KrakenTeamUser;
import com.jitterted.jittershout.adapter.twitch4j.Twitch4JTwitchTeam;
import com.jitterted.jittershout.domain.TwitchTeam;
import com.jitterted.jittershout.domain.TwitchUser;
import com.jitterted.jittershout.domain.UserId;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TwitchTeamTest {

  @Test
  public void teamNameIsShortNameWhenThereIsNoDisplayName() throws Exception {
    KrakenTeam krakenTeam = new KrakenTeam();
    krakenTeam.setName("shortname");
    krakenTeam.setUsers(Collections.emptyList());
    TwitchTeam twitchTeam = new Twitch4JTwitchTeam(new StubTeamFetcher(krakenTeam));

    assertThat(twitchTeam.name())
        .isEqualTo("shortname");
  }

  @Test
  public void teamNameIsDisplayNameWhenDisplayNameExists() throws Exception {
    KrakenTeam krakenTeam = new KrakenTeam();
    krakenTeam.setName("shortname");
    krakenTeam.setDisplayName("The Display Name");
    krakenTeam.setUsers(Collections.emptyList());
    TwitchTeam twitchTeam = new Twitch4JTwitchTeam(new StubTeamFetcher(krakenTeam));

    assertThat(twitchTeam.name())
        .isEqualTo("The Display Name");
  }

  @Test
  public void emptyTeamMeansNoUserIsMemberOfTeam() throws Exception {
    KrakenTeam krakenTeam = new KrakenTeam();
    krakenTeam.setUsers(Collections.emptyList());
    TwitchTeam twitchTeam = new Twitch4JTwitchTeam(new StubTeamFetcher(krakenTeam));

    assertThat(twitchTeam.isMember(UserId.from(1L)))
        .isFalse();
  }

  @Test
  public void teamWithUserIsMemberOfTeam() throws Exception {
    KrakenTeam krakenTeam = new KrakenTeam();
    KrakenTeamUser teamUser = new KrakenTeamUser();
    teamUser.setId(31L);
    krakenTeam.setUsers(List.of(teamUser));
    TwitchTeam twitchTeam = new Twitch4JTwitchTeam(new StubTeamFetcher(krakenTeam));

    assertThat(twitchTeam.isMember(UserId.from(31L)))
        .isTrue();
  }

  @Test
  public void nonEmptyTeamReturnsFalseForUserIdNotInTeam() throws Exception {
    KrakenTeam krakenTeam = new KrakenTeam();
    KrakenTeamUser teamUser = new KrakenTeamUser();
    teamUser.setId(11L);
    krakenTeam.setUsers(List.of(teamUser));
    TwitchTeam twitchTeam = new Twitch4JTwitchTeam(new StubTeamFetcher(krakenTeam));

    assertThat(twitchTeam.isMember(UserId.from(13L)))
        .isFalse();
  }

  @Test
  public void refreshedTeamHasNewUserAsTeamMember() throws Exception {
    KrakenTeam krakenTeam = new KrakenTeam();
    KrakenTeamUser teamUser = new KrakenTeamUser();
    teamUser.setId(11L);
    krakenTeam.setUsers(List.of(teamUser));
    TwitchTeam twitchTeam = new Twitch4JTwitchTeam(new StubTeamFetcher(krakenTeam));

    KrakenTeamUser newTeamUser = new KrakenTeamUser();
    newTeamUser.setId(17L);
    krakenTeam.setUsers(List.of(teamUser, newTeamUser));
    twitchTeam.refresh();

    assertThat(twitchTeam.isMember(UserId.from(17L)))
        .isTrue();
  }

  @Test
  public void nameAndUrlForUserReturnedForUserId() throws Exception {
    KrakenTeam krakenTeam = new KrakenTeam();
    KrakenTeamUser teamUser = new KrakenTeamUser();
    teamUser.setDisplayName("JitterTed");
    teamUser.setUrl("https://twitch.tv/jitterted");
    teamUser.setId(97L);
    krakenTeam.setUsers(List.of(teamUser));
    TwitchTeam twitchTeam = new Twitch4JTwitchTeam(new StubTeamFetcher(krakenTeam));

    TwitchUser twitchUser = twitchTeam.userById(UserId.from(97L));

    assertThat(twitchUser.name())
        .isEqualTo("JitterTed");
    assertThat(twitchUser.url())
        .isEqualTo("https://twitch.tv/jitterted");
  }

}
