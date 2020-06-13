package org.example.client.service;

import org.example.client.external.LocationExternalService;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final LocationExternalService locationExternalService;

    public UserService(LocationExternalService locationExternalService) {
        this.locationExternalService = locationExternalService;
    }

    public String getUser(String id) {

        locationExternalService.getLocations();
        return "Roshini";
    }
}
