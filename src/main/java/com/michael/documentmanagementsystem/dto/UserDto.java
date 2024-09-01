package com.michael.documentmanagementsystem.dto;

import com.michael.documentmanagementsystem.validationgroups.LoginInfo;
import com.michael.documentmanagementsystem.validationgroups.RegisterInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    @NotBlank(message = "first name can't be empty", groups = RegisterInfo.class)
    private String firstName;
    @NotBlank(message = "last name can't be empty", groups = RegisterInfo.class)
    private String lastName;
    @NotBlank(message = "email can't be empty", groups = {RegisterInfo.class, LoginInfo.class})
    @Email(message = "invalid email address", groups = {RegisterInfo.class, LoginInfo.class})
    private String email;
    @NotBlank(message = "password can't be empty", groups = {RegisterInfo.class, LoginInfo.class})
    private String password;
    @NotBlank(message = "National ID can't be empty", groups = RegisterInfo.class)
    @Pattern(regexp = "^\\d{16}+$", message = "National ID must be of 16 digits only", groups = RegisterInfo.class)
    private String NID;
    private String token;
}
