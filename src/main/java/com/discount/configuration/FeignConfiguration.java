package com.discount.configuration;

import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Value("${feign.client.maxAttempt}")
    private Integer maxAttempt;

    @Value("${feign.client.sleepTime}")
    private Long sleepTime;

    @Bean
    public Retryer clone() {
        return new FeignCustomRetryer(maxAttempt, sleepTime);
    }
}
