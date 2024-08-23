package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.config.filter.JwtService;
import com.michael.documentmanagementsystem.dto.LoginRequest;
import com.michael.documentmanagementsystem.dto.RegisterRequest;
import com.michael.documentmanagementsystem.dto.RegisterResponse;
import com.michael.documentmanagementsystem.mapper.UserMapper;
import com.michael.documentmanagementsystem.model.User;
import com.michael.documentmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public RegisterResponse register(RegisterRequest registerRequest)
    {
        registerRequest.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        User user = userMapper.registerRequestToUser(registerRequest);
        RegisterResponse registerResponse = userMapper.userToRegisterResponse(user);
        userRepository.save(user);
        return registerResponse;
    }

    public String login(LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if(authentication.isAuthenticated())
            return jwtService.generateToken(loginRequest.getEmail());
        else
            throw new UsernameNotFoundException("User Not Found");
    }
}
