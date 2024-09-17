package com.michael.documentmanagementsystem.mapper;

import com.michael.documentmanagementsystem.dto.UserDTO;
import com.michael.documentmanagementsystem.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(ignore = true, target = "password")
    UserDTO toDto(User user);

    User toEntity(UserDTO userDto);
}
