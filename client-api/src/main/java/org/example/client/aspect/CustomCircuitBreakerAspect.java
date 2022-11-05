package org.example.client.aspect;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
public class CustomCircuitBreakerAspect {

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;

    @Around("@annotation(org.example.client.annotation.CustomCircuitBreaker)")
    public Object createCustomRateLimiter(ProceedingJoinPoint proceedingJoinPoint) {

        CircuitBreakerConfig defaultConfig = circuitBreakerRegistry.getDefaultConfig();
        CircuitBreakerConfig overwrittenConfig = CircuitBreakerConfig
                .from(defaultConfig)
                .failureRateThreshold(5)
                .minimumNumberOfCalls(5)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .permittedNumberOfCallsInHalfOpenState(1)
                .waitDurationInOpenState(Duration.ofSeconds(2))
                .slidingWindowSize(10)
                .build();
//        CircuitBreakerRegistry circuitBreakerRegistry = circuitBreakerRegistry.(overwrittenConfig);
        CircuitBreaker circuitBreakerWithCustomConfig = circuitBreakerRegistry.circuitBreaker("nabe2", overwrittenConfig);

        CheckedFunction0<Object> decoratedSupplier = CircuitBreaker.decorateCheckedSupplier(circuitBreakerWithCustomConfig, proceedingJoinPoint::proceed);

        circuitBreakerWithCustomConfig.getEventPublisher()
                .onSuccess(event -> System.out.println(event.getCircuitBreakerName() + " " + event.getEventType()))
                .onStateTransition(event -> System.out.println(event.getCircuitBreakerName() + " " + event.getStateTransition().getToState()));

        return Try.of(decoratedSupplier)
                .get();
    }
}
