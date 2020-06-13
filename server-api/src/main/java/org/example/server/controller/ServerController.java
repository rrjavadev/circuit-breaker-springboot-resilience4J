package org.example.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/server-api/v1")
public class ServerController {

    @GetMapping("/locations")
    public List<String> getUser() {
        return Arrays.asList("London", "Hove", "Redhill");
    }
}
