package com.jitterted.jittershout;

import com.jitterted.jittershout.domain.TwitchTeam;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JittershoutApplication {

	public static void main(String[] args) {
		SpringApplication.run(JittershoutApplication.class, args);
	}

	public TwitchTeam createTwitch4jTeam() {
		// instantiate the JitterShoutOutBot
		return null;
	}
}
