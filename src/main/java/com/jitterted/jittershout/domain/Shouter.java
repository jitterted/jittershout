package com.jitterted.jittershout.domain;

public interface Shouter {
  void shoutOutTo(UserId id);

  void resetShoutOutTracking();

  int shoutOutTrackingCount();
}