package org.example.client.external;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class LocationExternalServiceImpl implements LocationExternalService {

    private final RestTemplate restTemplate;
    private CircuitBreakerFactory circuitBreakerFactory;

    public LocationExternalServiceImpl(RestTemplate restTemplate,
                                       CircuitBreakerFactory circuitBreakerFactory) {
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public List<String> getLocations() {

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("locationServiceCircuitbreaker");
        return circuitBreaker.run(() -> restTemplate.exchange("http://localhost:8082/server-api/v1/locations",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
                }), throwable -> ResponseEntity.accepted()
                .body(Collections.singletonList("default location"))).getBody();
    }
}
