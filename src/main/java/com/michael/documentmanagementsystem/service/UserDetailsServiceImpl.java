package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.model.User;
import com.michael.documentmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws BadCredentialsException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            System.out.println("User Not Found");
            throw new BadCredentialsException("User Not Found");
        }
        return user;
    }
}
