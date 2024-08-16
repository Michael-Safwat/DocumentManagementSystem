package com.michael.documentmanagementsystem.controller;

import com.michael.documentmanagementsystem.dto.LoginBody;
import com.michael.documentmanagementsystem.dto.UserDto;
import com.michael.documentmanagementsystem.model.User;
import com.michael.documentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody UserDto userDto)
    {
        return userService.register(userDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginBody loginBody)
    {
        return userService.login(loginBody);
    }

}
