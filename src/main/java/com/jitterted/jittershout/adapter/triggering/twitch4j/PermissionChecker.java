package com.jitterted.jittershout.adapter.triggering.twitch4j;

import com.github.twitch4j.common.enums.CommandPermission;

import java.util.Set;

public interface PermissionChecker {
  boolean allows(Set<CommandPermission> commandPermissions);
}
