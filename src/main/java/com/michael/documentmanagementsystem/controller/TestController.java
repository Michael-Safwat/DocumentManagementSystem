package com.michael.documentmanagementsystem.controller;

import io.jsonwebtoken.lang.Collections;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class TestController {

    @GetMapping("/testget")
    public String getGreeting()
    {
        return "Hello World";
    }

    @PostMapping("/testpost")
    public String postGreeting(@RequestBody String greeting)
    {
        return greeting;
    }
}
