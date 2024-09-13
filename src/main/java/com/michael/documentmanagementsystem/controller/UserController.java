package com.michael.documentmanagementsystem.controller;

import com.michael.documentmanagementsystem.dto.UserDto;
import com.michael.documentmanagementsystem.service.UserService;
import com.michael.documentmanagementsystem.validationgroups.LoginInfo;
import com.michael.documentmanagementsystem.validationgroups.RegisterInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Validated(RegisterInfo.class) UserDto userDto) {
        return new ResponseEntity<>(userService.register(userDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Validated(LoginInfo.class) UserDto userDto) {

        userDto = userService.login(userDto);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.AUTHORIZATION, userDto.getToken());
        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
    }

    @GetMapping("/users/{userEmail}")
    @PreAuthorize("#userEmail == authentication.principal.email")
    public ResponseEntity<UserDto> getUser(@PathVariable String userEmail) {
        return new ResponseEntity<>(userService.getUser(userEmail), HttpStatus.OK);
    }
}
