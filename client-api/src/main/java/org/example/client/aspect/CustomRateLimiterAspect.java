package org.example.client.aspect;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
public class CustomRateLimiterAspect {
    private RateLimiterRegistry rateLimiterRegistry;

//    @Around("@annotation(org.example.client.annotation.CustomCircuitBreaker)")
    public Object customRateLimiterConfig(ProceedingJoinPoint proceedingJoinPoint){

//        System.out.println("customRetryConfig:: Before invoking customRetryConfig() method");
        Object value = null;
        try {

            // Create a custom configuration for a RateLimiter
            RateLimiterConfig config = RateLimiterConfig.custom()
                    .timeoutDuration(Duration.ofSeconds(5))
                    .limitRefreshPeriod(Duration.ofSeconds(10))
                    .limitForPeriod(51)
                    .build();

            // Create a RateLimiterRegistry with a custom global configuration
            rateLimiterRegistry = RateLimiterRegistry.of(config);

            value = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }

//        System.out.println("customRetryConfig:: After invoking customRetryConfig() method. Return value="+value);
        return value;
    }
}
