package com.michael.documentmanagementsystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "first name can't be empty")
    private String firstName;
    @NotBlank(message = "last name can't be empty")
    private String lastName;
    @Email(message = "invalid email address")
    private String email;
    @NotBlank(message = "password can't be empty")
    private String password;
    @NotBlank(message ="National ID can't be empty")
    @Pattern(regexp = "^\\d{16}+$",message = "National ID must be of 16 digits only")
    private String NID;

}
