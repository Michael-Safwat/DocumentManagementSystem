package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.config.filter.JwtService;
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
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

   /* public UserService(UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserMapper userMapper)
    {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    }*/

    public UserDto register(UserDto userDto) {
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        userDto = userMapper.toDto(user);
        return userDto;
    }

    public UserDto login(UserDto userDto) {
        Authentication authentication =
                authenticationManager.
                        authenticate(new UsernamePasswordAuthenticationToken(
                                userDto.getEmail(),
                                userDto.getPassword()));

        if (authentication.isAuthenticated()) {
            User user = userRepository.findByEmail(userDto.getEmail());
            userDto = userMapper.toDto(user);
            userDto.setToken(jwtService.generateToken(userDto.getEmail()));
            return userDto;
        } else
            throw new UsernameNotFoundException("User Not Found");
    }

    public UserDto getUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        return userMapper.toDto(user);
    }
}
