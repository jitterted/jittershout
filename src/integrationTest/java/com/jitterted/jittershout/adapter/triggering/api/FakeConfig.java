package com.jitterted.jittershout.adapter.triggering.api;

import com.jitterted.jittershout.FakeShouter;
import com.jitterted.jittershout.JitterShoutOutBot;
import com.jitterted.jittershout.StubTwitchTeam;
import com.jitterted.jittershout.domain.Shouter;
import com.jitterted.jittershout.domain.TwitchTeam;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class FakeConfig {
  @Primary
  @Bean
  public JitterShoutOutBot mockBot() {
    return Mockito.mock(JitterShoutOutBot.class);
  }

  @Primary
  @Bean
  public TwitchTeam stubTwitch4jTeam() {
    return new StubTwitchTeam();
  }

  @Primary
  @Bean
  public Shouter fakeShouter() {
    return new FakeShouter();
  }
}
