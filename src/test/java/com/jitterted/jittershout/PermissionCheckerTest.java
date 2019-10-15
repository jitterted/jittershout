package com.jitterted.jittershout;

import com.github.twitch4j.common.enums.CommandPermission;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PermissionCheckerTest {

  @Test
  public void broadcasterBadgeIsAllowed() throws Exception {
    PermissionChecker permissionChecker = new DefaultPermissionChecker();

    assertThat(permissionChecker.allows(Set.of(CommandPermission.BROADCASTER)))
        .isTrue();
  }

  @Test
  public void everyoneBadgeIsNotAllowed() throws Exception {
    PermissionChecker permissionChecker = new DefaultPermissionChecker();

    assertThat(permissionChecker.allows(Set.of(CommandPermission.EVERYONE)))
        .isFalse();
  }

  @Test
  public void everyoneAndVipBadgeIsNotAllowed() throws Exception {
    PermissionChecker permissionChecker = new DefaultPermissionChecker();

    assertThat(permissionChecker.allows(Set.of(CommandPermission.EVERYONE, CommandPermission.VIP)))
        .isFalse();
  }

  @Test
  public void moderatorBadgeIsAllowed() throws Exception {
    PermissionChecker permissionChecker = new DefaultPermissionChecker();

    assertThat(permissionChecker.allows(Set.of(CommandPermission.MODERATOR)))
        .isTrue();
  }

  @Test
  public void moderatorWithVipBadgesIsAllowed() throws Exception {
    PermissionChecker permissionChecker = new DefaultPermissionChecker();

    assertThat(permissionChecker.allows(Set.of(CommandPermission.MODERATOR, CommandPermission.VIP)))
        .isTrue();
  }
}
