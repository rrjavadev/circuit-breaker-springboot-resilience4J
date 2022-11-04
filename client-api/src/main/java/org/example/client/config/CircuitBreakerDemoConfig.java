package org.example.client.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import io.github.resilience4j.fallback.autoconfigure.FallbackConfigurationOnMissingBean;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Configuration
@Import(FallbackConfigurationOnMissingBean.class)
/* Reference : https://github.com/resilience4j/resilience4j/issues/843*/
public class CircuitBreakerDemoConfig {

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    RetryRegistry retryRegistry;

    @Bean
    public CircuitBreaker defaultCircuitBreaker() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slidingWindowSize(5)
                .waitDurationInOpenState(Duration.ofMillis(6000))
                .recordExceptions(IOException.class, TimeoutException.class)
                .build();

        return circuitBreakerRegistry.circuitBreaker("serviceName", circuitBreakerConfig);
    }

    @Bean
    public Retry defaultRetry() {

        RetryConfig config = RetryConfig.custom()
                .maxAttempts(6)
                .build();

        return retryRegistry.retry("serviceName", config);
    }

    @Bean
    public CircuitBreakerConfigCustomizer testCustomizer() {

        return CircuitBreakerConfigCustomizer
                .of("test", builder -> builder.slidingWindowSize(100));
    }

}
