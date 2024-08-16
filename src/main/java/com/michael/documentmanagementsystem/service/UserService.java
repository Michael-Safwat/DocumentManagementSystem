package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.config.filter.JwtService;
import com.michael.documentmanagementsystem.dto.LoginBody;
import com.michael.documentmanagementsystem.dto.UserDto;
import com.michael.documentmanagementsystem.model.User;
import com.michael.documentmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public User register(UserDto userDto)
    {
        User user = new User(
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getUsername(),
                userDto.getEmail(),
                bCryptPasswordEncoder.encode(userDto.getPassword()),
                userDto.getNID());
        userRepository.save(user);
        return user;
    }

    public String login(LoginBody loginBody) {

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginBody.getUsername(),loginBody.getPassword()));

        if(authentication.isAuthenticated())
            return jwtService.generateToken(loginBody.getUsername());
        else {
            return "User not Found";
        }
    }
}
