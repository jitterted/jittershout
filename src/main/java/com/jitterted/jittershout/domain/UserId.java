package com.jitterted.jittershout.domain;

import lombok.Data;

@Data
public class UserId {
  private final String id;

  public static UserId from(long id) {
    return new UserId(String.valueOf(id));
  }

}
