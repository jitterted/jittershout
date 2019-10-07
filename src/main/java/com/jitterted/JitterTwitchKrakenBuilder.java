package com.jitterted;

import com.github.twitch4j.common.builder.TwitchAPIBuilder;
import com.github.twitch4j.common.feign.interceptor.TwitchClientIdInterceptor;
import com.github.twitch4j.kraken.TwitchKraken;
import com.github.twitch4j.kraken.TwitchKrakenErrorDecoder;
import com.netflix.config.ConfigurationManager;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class JitterTwitchKrakenBuilder extends TwitchAPIBuilder<JitterTwitchKrakenBuilder> {

    /**
     * BaseUrl
     */
    private String baseUrl = "https://api.twitch.tv/kraken";

    /**
     * Default Timeout
     */
    @With
    private Integer timeout = 5000;

    /**
     * Initialize the builder
     *
     * @return Twitch Kraken Builder
     */
    public static JitterTwitchKrakenBuilder builder() {
        return new JitterTwitchKrakenBuilder();
    }

    /**
     * Twitch API Client (Kraken)
     *
     * @return TwitchKraken
     */
    public TwitchKraken build() {
        log.debug("Kraken: Initializing Module ...");

        // Hystrix
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", timeout);
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.requestCache.enabled", false);

        // Build
        TwitchKraken client = HystrixFeign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .logger(new Logger.ErrorLogger())
            .errorDecoder(new TwitchKrakenErrorDecoder(new JacksonDecoder()))
            .requestInterceptor(new TwitchClientIdInterceptor(this))
            .options(new Request.Options(timeout / 3, timeout))
            .retryer(new Retryer.Default(500, timeout, 2))
            .target(JitterTwitchKraken.class, baseUrl);

        // register with serviceMediator
        getEventManager().getServiceMediator().addService("twitch4j-kraken", client);

        return client;
    }
}
