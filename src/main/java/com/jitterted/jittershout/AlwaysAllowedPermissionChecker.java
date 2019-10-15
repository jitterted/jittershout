package com.jitterted.jittershout;

import com.github.twitch4j.common.enums.CommandPermission;

import java.util.Set;

class AlwaysAllowedPermissionChecker implements PermissionChecker {
  @Override
  public boolean allows(Set<CommandPermission> commandPermissions) {
    return true;
  }
}
