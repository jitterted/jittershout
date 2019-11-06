package com.jitterted.jittershout.adapter.triggering.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotInfoDto {
  private boolean shoutOutActive;
  private int shoutOutCount;
}
