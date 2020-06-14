package org.example.client.external;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class LocationExternalServiceImpl implements LocationExternalService {

    private final RestTemplate restTemplate;

    public LocationExternalServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<String> getLocations() {
        return restTemplate.exchange("http://localhost:8082/server-api/v1/locations",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
                }).getBody();
    }
}
