package com.michael.documentmanagementsystem.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public class UserDto {

    @NotBlank(message = "first name can't be empty")
    private String firstName;
    @NotBlank(message = "last name can't be empty")
    private String lastName;
    @NotBlank(message = "username can't be empty")
    private String username;
    @Email(message = "invalid email address")
    private String email;
    @NotBlank(message = "password can't be empty")
    private String password;
    @NotNull
    /*@Min(value = 15,message = "NID must be 16 characters long")
    @Max(value = 17,message = "NID must be 16 characters long")*/
    //TODO: VALIDATION FOR NID
    private Long NID;

    public UserDto() {
    }


    public UserDto(String firstName, String lastName, String username, String email, String password, Long NID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.NID = NID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getNID() {
        return NID;
    }

    public void setNID(Long NID) {
        this.NID = NID;
    }
}
