package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.config.filter.JwtService;
import com.michael.documentmanagementsystem.dto.LoginDto;
import com.michael.documentmanagementsystem.dto.UserDto;
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

    public UserDto register(UserDto userDto)
    {
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        User user = userMapper.toEntity(userDto);
        userRepository.save(user);
        return userDto;
    }

    public String login(LoginDto loginDto) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        if(authentication.isAuthenticated())
            return jwtService.generateToken(loginDto.getUsername());
        else
            throw new UsernameNotFoundException("User Not Found");
    }
}
