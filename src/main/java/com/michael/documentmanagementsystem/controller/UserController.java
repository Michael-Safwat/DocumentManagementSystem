package com.michael.documentmanagementsystem.controller;

import com.michael.documentmanagementsystem.dto.LoginRequest;
import com.michael.documentmanagementsystem.dto.RegisterRequest;
import com.michael.documentmanagementsystem.dto.RegisterResponse;
import com.michael.documentmanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest registerRequest)
    {
        return new ResponseEntity<>(userService.register(registerRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest)
    {
        return new ResponseEntity<>(userService.login(loginRequest),HttpStatus.OK);
    }
}
