package com.michael.documentmanagementsystem.mapper;

import com.michael.documentmanagementsystem.dto.UserDto;
import com.michael.documentmanagementsystem.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto userDto);
    UserDto toDto(User user);
}
