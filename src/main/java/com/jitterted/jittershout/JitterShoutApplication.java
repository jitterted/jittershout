package com.jitterted.jittershout;

import com.jitterted.jittershout.adapter.triggering.twitch4j.TwitchConfiguration;
import com.jitterted.jittershout.domain.Shouter;
import com.jitterted.jittershout.domain.TwitchTeam;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JitterShoutApplication {

	public static void main(String[] args) {
		SpringApplication.run(JitterShoutApplication.class, args);
	}

	@Bean
	public JitterShoutOutBot createBot(TwitchConfiguration twitchConfiguration) {
		return JitterShoutOutBot.create(twitchConfiguration);
	}

	@Bean
	public TwitchTeam createTwitch4jTeam(JitterShoutOutBot jitterShoutOutBot) {
		return jitterShoutOutBot.twitchTeam();
	}

	@Bean
	public Shouter createShouter(JitterShoutOutBot jitterShoutOutBot) {
		return jitterShoutOutBot.shouter();
	}

}
