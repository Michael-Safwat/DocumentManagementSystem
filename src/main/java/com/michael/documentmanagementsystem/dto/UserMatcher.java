package com.michael.documentmanagementsystem.dto;

public class UserMatcher {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Long NID;

    public UserMatcher() {
    }

    public UserMatcher(String firstName, String lastName, String email, String password, Long NID) {
        this.firstName = firstName;
        this.lastName = lastName;
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
