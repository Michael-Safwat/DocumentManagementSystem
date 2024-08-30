package com.michael.documentmanagementsystem.dto;

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
    @NotBlank(message = "first name can't be empty")
    private String firstName;
    @NotBlank(message = "last name can't be empty")
    private String lastName;
    @NotBlank(message = "email can't be empty")
    @Email(message = "invalid email address")
    private String email;
    @NotBlank(message = "password can't be empty")
    private String password;
    @NotBlank(message = "National ID can't be empty")
    @Pattern(regexp = "^\\d{16}+$", message = "National ID must be of 16 digits only")
    private String NID;
    private String token;
}
