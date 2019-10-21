package com.jitterted.jittershout;

public class TwitchUser {

  private final UserId userId;
  private final String name;
  private final String url;

  public TwitchUser(UserId userId, String name, String url) {
    this.userId = userId;
    this.name = name;
    this.url = url;
  }

  public String name() {
    return name;
  }

  public String url() {
    return url;
  }

  public UserId userId() {
    return userId;
  }
}
