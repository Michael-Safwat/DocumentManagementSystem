package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.config.filter.JwtService;
import com.michael.documentmanagementsystem.dto.LoginBody;
import com.michael.documentmanagementsystem.dto.UserMatcher;
import com.michael.documentmanagementsystem.model.AppUser;
import com.michael.documentmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public AppUser register(UserMatcher userMatcher)
    {
        AppUser appUser = new AppUser(
                userMatcher.getFirstName(),
                userMatcher.getLastName(),
                userMatcher.getEmail(),
                bCryptPasswordEncoder.encode(userMatcher.getPassword()),
                userMatcher.getNID());
        userRepository.save(appUser);
        return appUser;
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
