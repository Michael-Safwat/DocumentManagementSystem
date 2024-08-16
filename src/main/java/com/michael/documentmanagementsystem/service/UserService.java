package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.dto.UserMatcher;
import com.michael.documentmanagementsystem.model.AppUser;
import com.michael.documentmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByEmail(email);

        if(appUser== null)
        {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User Not Found");
        }
        return appUser;
    }

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

    /*public AppUser login(UserMatcher userMatcher) {

    }*/
}
