package com.jitterted.jittershout;

import com.github.twitch4j.kraken.domain.KrakenTeam;
import com.github.twitch4j.kraken.domain.KrakenTeamUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShouterTest {

  @Test
  public void userNotInTeamMeansNoShoutOut() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);

    Shouter shouter = new Shouter(senderSpy, new StubTwitchTeam(false), new BotStatus(true));

    shouter.shoutOutTo(UserId.from(0L));

    verify(senderSpy, never()).send(any());
  }

  @Test
  public void userInTeamTriggersShoutOutToThatUser() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);

    Shouter shouter = new Shouter(senderSpy, new StubTwitchTeam(true), new BotStatus(true));

    shouter.shoutOutTo(UserId.from(37L));

    verify(senderSpy).send("Hey, it's JitterTed, a member of the stub team! Check out their stream at https://twitch.tv/jitterted");
  }

  @Test
  public void userInTeamGetsShoutedAtOnlyOnce() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);

    Shouter shouter = new Shouter(senderSpy, new StubTwitchTeam(true), new BotStatus(true));

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

    Shouter shouter = new Shouter(senderSpy, new StubTwitchTeam(false), botStatus);

    shouter.shoutOutTo(UserId.from(57L));

    verify(senderSpy, never()).send(any());
  }

  private static class StubTwitchTeam implements TwitchTeam {
    private final boolean defaultIsMember;

    public StubTwitchTeam(boolean defaultIsMember) {
      this.defaultIsMember = defaultIsMember;
    }

    @Override
    public String name() {
      return "stub";
    }

    @Override
    public boolean isMember(UserId userId) {
      return defaultIsMember;
    }

    @Override
    public void refresh() {
    }

    @Override
    public TwitchUser userById(UserId userId) {
      return new TwitchUser(userId, "JitterTed", "https://twitch.tv/jitterted");
    }
  }
}
