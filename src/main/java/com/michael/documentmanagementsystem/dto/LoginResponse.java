package com.michael.documentmanagementsystem.dto;

public class LoginResponse {
    private String token;
    private String email;
    private Long NID;

    public LoginResponse() {
    }

    public LoginResponse(String token, String email, Long NID) {
        this.token = token;
        this.email = email;
        this.NID = NID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getNID() {
        return NID;
    }

    public void setNID(Long NID) {
        this.NID = NID;
    }
}
