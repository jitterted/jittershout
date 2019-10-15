package com.jitterted.jittershout;

import com.github.twitch4j.chat.TwitchChat;

public class TwitchChatMessageSender implements MessageSender {
  private final TwitchChat twitchChat;
  private final String channel;

  public TwitchChatMessageSender(TwitchChat twitchChat, String channel) {
    this.twitchChat = twitchChat;
    this.channel = channel;
  }

  @Override
  public void send(String message) {
    twitchChat.sendMessage(channel, message);
  }
}
