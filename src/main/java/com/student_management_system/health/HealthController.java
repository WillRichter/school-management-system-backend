package com.student_management_system.health;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthController {

    public HealthController() {
    }

    @GetMapping
    public ResponseEntity<String> index() {
        return new ResponseEntity<>("Backend available", HttpStatus.OK);
    }

}
