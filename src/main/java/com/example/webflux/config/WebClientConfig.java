package com.example.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClientConfig
 *
 * @author yonse
 * @date 2025-10-11
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient getWebClient(WebClient.Builder builder) {
        return builder.build();
    }
}