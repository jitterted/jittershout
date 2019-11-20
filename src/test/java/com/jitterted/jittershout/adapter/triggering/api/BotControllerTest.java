package com.jitterted.jittershout.adapter.triggering.api;

import com.jitterted.jittershout.StubTwitchTeam;
import com.jitterted.jittershout.domain.DefaultShouter;
import com.jitterted.jittershout.domain.MessageSender;
import com.jitterted.jittershout.domain.Shouter;
import com.jitterted.jittershout.domain.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BotControllerTest {

  @Test
  public void turnOffShoutOutViaPostMeansNoShoutOutToTeamMembers() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);
    Shouter shouter = new DefaultShouter(senderSpy, new StubTwitchTeam(true));
    BotController botController = new BotController(shouter);

    BotInfoDto botInfoDto = new BotInfoDto();
    botInfoDto.setShoutOutActive(false);
    botController.updateBotState(botInfoDto);

    shouter.shoutOutTo(UserId.from(57L));

    verify(senderSpy, never()).send(any());
  }
}
