package com.jitterted.jittershout.adapter.triggering.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamInfoDto {
  private final String name;
  private final String count;
}
