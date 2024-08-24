package com.michael.documentmanagementsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/testget")
    public ResponseEntity<String> getGreeting() {
        return new ResponseEntity<>("Hello World",HttpStatus.OK);
    }

    @PostMapping("/testpost")
    public ResponseEntity<String> postGreeting(@RequestBody String greeting) {
        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }
}
