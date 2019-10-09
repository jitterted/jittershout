package com.jitterted;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShouterTest {

  @Test
  public void userNotInTeamMeansNoShoutOut() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);
    TwitchTeam team = new TwitchTeam();
    team.setUsers(Collections.emptyList());

    Shouter shouter = new Shouter(senderSpy, team);

    shouter.shoutTo(0L);

    verify(senderSpy, never()).send(any());
  }

  @Test
  public void userInTeamTriggersShoutOutToThatUser() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);
    TwitchTeam team = new TwitchTeam();
    team.setDisplayName("Live Coders");
    TwitchUser teamUser = new TwitchUser();
    teamUser.setDisplayName("JitterTed");
    teamUser.setUrl("https://twitch.tv/jitterted");
    teamUser.setId(37L);
    team.setUsers(List.of(teamUser));

    Shouter shouter = new Shouter(senderSpy, team);

    shouter.shoutTo(37L);

    verify(senderSpy).send("Hey, it's JitterTed, a member of the Live Coders team! Check out their stream at https://twitch.tv/jitterted");
  }

  @Test
  public void userInTeamGetsShoutedAtOnlyOnce() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);
    TwitchTeam team = new TwitchTeam();
    TwitchUser user = new TwitchUser();
    user.setId(73L);
    team.setUsers(List.of(user));

    Shouter shouter = new Shouter(senderSpy, team);

    shouter.shoutTo(73L);
    shouter.shoutTo(73L);

    verify(senderSpy, times(1)).send(any());
  }
}
