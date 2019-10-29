package com.jitterted.jittershout;

import com.jitterted.jittershout.domain.TwitchTeam;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JittershoutApplication {

	public static void main(String[] args) {
		SpringApplication.run(JittershoutApplication.class, args);
	}

	@Bean
	public TwitchTeam createTwitch4jTeam() {
		JitterShoutOutBot jitterShoutOutBot = JitterShoutOutBot.create();
		return jitterShoutOutBot.twitchTeam();
	}
}
