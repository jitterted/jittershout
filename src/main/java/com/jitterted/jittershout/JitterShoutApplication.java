package com.jitterted.jittershout;

import com.jitterted.jittershout.domain.BotStatus;
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
	public TwitchTeam createTwitch4jTeam(BotStatus botStatus) {
		JitterShoutOutBot jitterShoutOutBot = JitterShoutOutBot.create(botStatus);
		return jitterShoutOutBot.twitchTeam();
	}

	@Bean
	public BotStatus createBotStatus() {
		return new BotStatus(true);
	}
}
