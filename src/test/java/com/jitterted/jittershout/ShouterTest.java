package com.jitterted.jittershout;

import com.jitterted.jittershout.domain.DefaultShouter;
import com.jitterted.jittershout.domain.MessageSender;
import com.jitterted.jittershout.domain.Shouter;
import com.jitterted.jittershout.domain.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ShouterTest {

  @Test
  public void userNotInTeamMeansNoShoutOut() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);

    Shouter shouter = new DefaultShouter(senderSpy, new StubTwitchTeam(false));

    shouter.shoutOutTo(UserId.from(0L));

    verify(senderSpy, never()).send(any());
  }

  @Test
  public void userInTeamTriggersShoutOutToThatUser() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);

    Shouter shouter = new DefaultShouter(senderSpy, new StubTwitchTeam(true));

    shouter.shoutOutTo(UserId.from(37L));

    verify(senderSpy).send("Hey, it's JitterTed, a member of the stub team! Check out their stream at https://twitch.tv/jitterted");
  }

  @Test
  public void userInTeamGetsShoutedAtOnlyOnce() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);

    Shouter shouter = new DefaultShouter(senderSpy, new StubTwitchTeam(true));

    shouter.shoutOutTo(UserId.from(73L));
    shouter.shoutOutTo(UserId.from(73L));

    verify(senderSpy, times(1)).send(any());
  }

  @Test
  public void userInTeamDoesNotGetShoutOutIfShoutOutIsDisabled() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);

    Shouter shouter = new DefaultShouter(senderSpy, new StubTwitchTeam(true));
    shouter.changeShoutOutActiveTo(false);

    shouter.shoutOutTo(UserId.from(57L));

    verify(senderSpy, never()).send(any());
  }

  @Test
  public void userInTeamGetsShoutOutAfterReset() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);

    Shouter shouter = new DefaultShouter(senderSpy, new StubTwitchTeam(true));

    shouter.shoutOutTo(UserId.from(67L));
    shouter.resetShoutOutTracking();
    shouter.shoutOutTo(UserId.from(67L));

    verify(senderSpy, times(2)).send(any());
  }

}
