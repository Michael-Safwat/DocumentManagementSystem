package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.config.filter.JwtService;
import com.michael.documentmanagementsystem.dto.UserDTO;
import com.michael.documentmanagementsystem.mapper.UserMapper;
import com.michael.documentmanagementsystem.model.User;
import com.michael.documentmanagementsystem.repository.UserRepository;
import com.michael.documentmanagementsystem.service.util.UtilService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UtilService utilService;

    public UserService(UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserMapper userMapper,
                       UtilService utilService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        this.utilService = utilService;
    }

    public UserDTO register(UserDTO userDto) {
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        User user = userMapper.toEntity(userDto);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        user.setCreatedAt(LocalDateTime.now().format(formatter));
        user = userRepository.save(user);
        userDto = userMapper.toDto(user);

        return userDto;
    }

    public UserDTO login(UserDTO userDto) {
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

    public UserDTO getUser() {
        User user = userRepository.findByNID(utilService.getNID());
        return userMapper.toDto(user);
    }
}
