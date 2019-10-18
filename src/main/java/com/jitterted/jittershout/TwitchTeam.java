package com.jitterted.jittershout;

import com.github.twitch4j.kraken.domain.KrakenTeam;
import com.github.twitch4j.kraken.domain.KrakenTeamUser;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class TwitchTeam {
  private final TeamFetcher teamFetcher;
  private String teamName;
  private Set<UserId> userIds;

  public TwitchTeam(TeamFetcher teamFetcher) {
    this.teamFetcher = teamFetcher;
    refresh();
  }

  public String name() {
    return teamName;
  }

  public boolean isMember(UserId userId) {
    return userIds.contains(userId);
  }

  public void refresh() {
    KrakenTeam krakenTeam = teamFetcher.fetch();
    teamName = extractTeamNameFrom(krakenTeam);
    userIds = extractUserIdsFrom(krakenTeam);
  }

  private String extractTeamNameFrom(KrakenTeam krakenTeam) {
    return krakenTeam.getDisplayName() != null ? krakenTeam.getDisplayName() : krakenTeam.getName();
  }

  @NotNull
  private Set<UserId> extractUserIdsFrom(KrakenTeam krakenTeam) {
    return krakenTeam.getUsers().stream()
                     .map(KrakenTeamUser::getId)
                     .map(UserId::from)
                     .collect(Collectors.toSet());
  }
}
