package com.jitterted;

import com.github.twitch4j.chat.events.CommandEvent;
import com.github.twitch4j.kraken.domain.KrakenTeam;
import com.github.twitch4j.kraken.domain.KrakenTeamUser;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BotCommandHandlerTest {

  public static final MessageSender DUMMY_MESSAGE_SENDER = Mockito.mock(MessageSender.class);

  @Test
  public void statusCommandSendsStatusMessageWithTeamSizeAndName() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);
    KrakenTeam krakenTeam = new KrakenTeam();
    krakenTeam.setName("livecoders");
    krakenTeam.setUsers(List.of(new KrakenTeamUser(), new KrakenTeamUser()));

    BotCommandHandler botCommandHandler = new BotCommandHandler(senderSpy, krakenTeam, new BotStatus(true));

    botCommandHandler.handle(createCommandWithText("sob status"));

    verify(senderSpy).send("Shout-out is on, for the 2 team members of 'livecoders'.");
  }

  @Test
  public void whenShoutOutIsDisabledStatusSaysItsOff() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);
    KrakenTeam krakenTeam = new KrakenTeam();
    krakenTeam.setUsers(Collections.emptyList());

    BotCommandHandler botCommandHandler = new BotCommandHandler(senderSpy, krakenTeam, new BotStatus(false));

    botCommandHandler.handle(createCommandWithText("sob status"));

    verify(senderSpy).send(startsWith("Shout-out is off"));
  }

  @ParameterizedTest
  @CsvSource({"sob off, false", "sob on, true"})
  public void sobOffCommandDisablesShoutOut(String command, boolean expectedEnabled) throws Exception {
    BotStatus botStatus = BotStatus.builder().shoutOutEnabled(true).build();
    BotCommandHandler botCommandHandler = new BotCommandHandler(DUMMY_MESSAGE_SENDER, null, botStatus);

    botCommandHandler.handle(createCommandWithText(command));

    assertThat(botStatus.isShoutOutEnabled())
        .isEqualTo(expectedEnabled);
  }

  @ParameterizedTest
  @ValueSource(strings = {"on", "off"})
  public void sobCommandIsAcknowledgedWithNewStatusMessage(String state) throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);
    KrakenTeam krakenTeam = new KrakenTeam();
    krakenTeam.setUsers(Collections.emptyList());

    BotCommandHandler botCommandHandler = new BotCommandHandler(senderSpy, krakenTeam, new BotStatus(false));

    botCommandHandler.handle(createCommandWithText("sob " + state));

    verify(senderSpy).send("Shout-out is now " + state);
  }

  @Test
  public void sobCommandWithoutSubcommandIsIgnored() throws Exception {
    MessageSender senderSpy = Mockito.mock(MessageSender.class);
    KrakenTeam krakenTeam = new KrakenTeam();
    krakenTeam.setUsers(Collections.emptyList());

    BotCommandHandler botCommandHandler = new BotCommandHandler(senderSpy, krakenTeam, new BotStatus(true));

    botCommandHandler.handle(createCommandWithText("sob"));

    verify(senderSpy, never()).send(any());
  }

  @NotNull
  private CommandEvent createCommandWithText(String commandText) {
    return new CommandEvent(null, null, null, null, commandText, Collections.emptySet());
  }
}