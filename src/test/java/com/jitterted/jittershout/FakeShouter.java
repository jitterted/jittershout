package com.jitterted.jittershout;

import com.jitterted.jittershout.domain.Shouter;
import com.jitterted.jittershout.domain.UserId;

public class FakeShouter implements Shouter {
  private int count = 1;
  private boolean active = true;

  public void shoutOutTo(UserId id) {
  }

  public void resetShoutOutTracking() {
    count = 0;
  }

  @Override
  public int shoutOutTrackingCount() {
    return count;
  }

  @Override
  public void changeShoutOutActiveTo(boolean isActive) {
    active = isActive;
  }

  @Override
  public boolean isShoutOutActive() {
    return active;
  }
}
