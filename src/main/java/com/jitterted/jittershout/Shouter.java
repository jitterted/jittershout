package com.jitterted.jittershout;

public interface Shouter {
  void shoutOutTo(UserId id);

  void resetShoutOutTracking();

  int shoutOutTrackingCount();
}
