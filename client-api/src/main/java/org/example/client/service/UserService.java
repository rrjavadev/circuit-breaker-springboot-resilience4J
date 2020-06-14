package org.example.client.service;

import lombok.extern.slf4j.Slf4j;
import org.example.client.external.LocationExternalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final LocationExternalService locationExternalService;

    public UserService(LocationExternalService locationExternalService) {
        this.locationExternalService = locationExternalService;
    }

    public String getUser(String id) {

        List<String> locations = locationExternalService.getLocations();
        locations.forEach(e-> log.info(e));
        return locations.get(0);
    }
}
