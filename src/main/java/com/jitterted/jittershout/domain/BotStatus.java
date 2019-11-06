package com.jitterted.jittershout.domain;

import lombok.Data;

@Data
public class BotStatus {
  private boolean shoutOutActive;

  public BotStatus(boolean shoutOutActive) {
    this.shoutOutActive = shoutOutActive;
  }
}
