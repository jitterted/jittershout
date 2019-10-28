package com.jitterted.jittershout.adapter.triggering.twitch4j;

import com.github.twitch4j.kraken.domain.KrakenTeam;
import com.github.twitch4j.kraken.domain.KrakenTeamUser;
import com.jitterted.jittershout.domain.TwitchTeam;
import com.jitterted.jittershout.domain.TwitchUser;
import com.jitterted.jittershout.domain.UserId;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Twitch4JTwitchTeam implements TwitchTeam {
  private final TeamFetcher teamFetcher;
  private String teamName;
  private Map<UserId, TwitchUser> twitchUsers;

  public Twitch4JTwitchTeam(TeamFetcher teamFetcher) {
    this.teamFetcher = teamFetcher;
    refresh();
  }

  @Override
  public String name() {
    return teamName;
  }

  @Override
  public boolean isMember(UserId userId) {
    return twitchUsers.containsKey(userId);
  }

  @Override
  public void refresh() {
    KrakenTeam krakenTeam = teamFetcher.fetch();
    teamName = extractTeamNameFrom(krakenTeam);
    twitchUsers = extractTwitchUsersFrom(krakenTeam);
  }

  @Override
  public TwitchUser userById(UserId userId) {
    return twitchUsers.get(userId);
  }

  @Override
  public int count() {
    return twitchUsers.size();
  }

  @NotNull
  private Map<UserId, TwitchUser> extractTwitchUsersFrom(KrakenTeam krakenTeam) {
    return krakenTeam.getUsers()
                     .stream()
                     .map(this::from)
                     .collect(Collectors.toMap(TwitchUser::userId, Function.identity()));
  }

  private TwitchUser from(KrakenTeamUser krakenTeamUser) {
    return new TwitchUser(UserId.from(krakenTeamUser.getId()),
                          krakenTeamUser.getDisplayName(),
                          krakenTeamUser.getUrl());
  }

  private String extractTeamNameFrom(KrakenTeam krakenTeam) {
    return krakenTeam.getDisplayName() != null ? krakenTeam.getDisplayName() : krakenTeam.getName();
  }

}
