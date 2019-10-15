package com.jitterted;

import com.github.twitch4j.kraken.domain.KrakenTeam;
import com.github.twitch4j.kraken.domain.KrakenTeamUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShouterTest {

  @Test
  public void userNotInTeamMeansNoShoutOut() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);
    KrakenTeam team = new KrakenTeam();
    team.setUsers(Collections.emptyList());

    Shouter shouter = new Shouter(senderSpy, team, new BotStatus(true));
    // resetting the spy to make sure that anything that might have been sent
    // during construction is ignored by this test
    reset(senderSpy);

    shouter.shoutOutTo(UserId.from(0L));

    verify(senderSpy, never()).send(any());
  }

  @Test
  public void userInTeamTriggersShoutOutToThatUser() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);
    KrakenTeam team = new KrakenTeam();
    team.setDisplayName("Live Coders");
    KrakenTeamUser teamUser = new KrakenTeamUser();
    teamUser.setDisplayName("JitterTed");
    teamUser.setUrl("https://twitch.tv/jitterted");
    teamUser.setId(37L);
    team.setUsers(List.of(teamUser));

    Shouter shouter = new Shouter(senderSpy, team, new BotStatus(true));

    shouter.shoutOutTo(UserId.from(37L));

    verify(senderSpy).send("Hey, it's JitterTed, a member of the Live Coders team! Check out their stream at https://twitch.tv/jitterted");
  }

  @Test
  public void userInTeamGetsShoutedAtOnlyOnce() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);
    KrakenTeam team = new KrakenTeam();
    KrakenTeamUser user = new KrakenTeamUser();
    user.setId(73L);
    team.setUsers(List.of(user));

    Shouter shouter = new Shouter(senderSpy, team, new BotStatus(true));
    // resetting the spy to make sure that anything that might have been sent
    // during construction is ignored by this test
    reset(senderSpy);

    shouter.shoutOutTo(UserId.from(73L));
    shouter.shoutOutTo(UserId.from(73L));

    verify(senderSpy, times(1)).send(any());
  }

  @Test
  public void userInTeamDoesNotGetShoutOutIfShoutOutIsDisabled() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);
    KrakenTeam team = new KrakenTeam();
    KrakenTeamUser teamUser = new KrakenTeamUser();
    teamUser.setDisplayName("JitterTed");
    teamUser.setId(57L);
    team.setUsers(List.of(teamUser));

    BotStatus botStatus = BotStatus.builder().shoutOutEnabled(false).build();

    Shouter shouter = new Shouter(senderSpy, team, botStatus);
    // resetting the spy to make sure that anything that might have been sent
    // during construction is ignored by this test
    reset(senderSpy);

    shouter.shoutOutTo(UserId.from(57L));

    verify(senderSpy, never()).send(any());
  }
}
