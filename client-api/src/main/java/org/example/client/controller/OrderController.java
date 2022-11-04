package org.example.client.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.example.client.annotation.CustomRateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private static final String ORDER_SERVICE = "orderService";
    private AtomicInteger attempts = new AtomicInteger(0);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/order-circuit-breaker")
    @CircuitBreaker(name=ORDER_SERVICE, fallbackMethod = "circuitBreakerFallback")
    public ResponseEntity<String> createOrder(){
        String response = restTemplate.getForObject("http://localhost:8081/payment", String.class);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
    public ResponseEntity<String> circuitBreakerFallback(Exception e){
        return new ResponseEntity<String>("Payment service is down", HttpStatus.OK);

    }

    @GetMapping("/order-bulkhead")
    @Bulkhead(name=ORDER_SERVICE,fallbackMethod = "bulkHeadFallback")
    public ResponseEntity<String> createOrderBulkhead()
    {
        String response = restTemplate.getForObject("http://localhost:8081/payment", String.class);
        logger.info(LocalTime.now() + " Call processing finished = " + Thread.currentThread().getName());
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> bulkHeadFallback(Exception t)
    {
        return new ResponseEntity<String>(" orderService is full and does not permit further calls", HttpStatus.TOO_MANY_REQUESTS);
    }

    @GetMapping("/order-rate-limiter")
//    @RateLimiter(name=ORDER_SERVICE, fallbackMethod = "rateLimiterFallback")
    @CustomRateLimiter
    public ResponseEntity<String> createOrderRateLimiter()
    {
        String response = restTemplate.getForObject("http://localhost:8081/payment", String.class);
        logger.info(LocalTime.now() + " Call processing finished = " + Thread.currentThread().getName());
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> rateLimiterFallback(Exception e){
        return new ResponseEntity<String>("order service does not permit further calls", HttpStatus.TOO_MANY_REQUESTS);

    }

    @GetMapping("/order-retry")
    @Retry(name = ORDER_SERVICE,fallbackMethod = "fallback_retry")
    public ResponseEntity<String> createOrderRetry(){
        logger.info("Payment service call attempted:::"+ attempts.incrementAndGet());
        String response = restTemplate.getForObject("http://localhost:8081/payment", String.class);
        logger.info("Payment service called");
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> fallback_retry(Exception e){

        attempts.set(0);
        return new ResponseEntity<String>("Payment service is down", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
