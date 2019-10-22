package com.jitterted.jittershout.adapter.twitch4j;

import com.github.twitch4j.common.enums.CommandPermission;

import java.util.Collections;
import java.util.Set;

public class DefaultPermissionChecker implements PermissionChecker {

  // CommandPermissions come from chat badges, see: https://help.twitch.tv/s/article/twitch-chat-badges-guide?language=en_US

  private static final Set<CommandPermission> ALLOWED_PERMISSIONS =
      Set.of(CommandPermission.BROADCASTER,
             CommandPermission.MODERATOR);

  @Override
  public boolean allows(Set<CommandPermission> commandPermissions) {
    return !Collections.disjoint(ALLOWED_PERMISSIONS, commandPermissions);
  }
}
