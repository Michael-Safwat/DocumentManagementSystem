package com.michael.documentmanagementsystem.mapper;

import com.michael.documentmanagementsystem.dto.RegisterRequest;
import com.michael.documentmanagementsystem.dto.RegisterResponse;
import com.michael.documentmanagementsystem.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User registerRequestToUser(RegisterRequest registerRequest);
    RegisterResponse userToRegisterResponse(User user);
}
