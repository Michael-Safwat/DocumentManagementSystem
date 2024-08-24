package com.michael.documentmanagementsystem.dto;

public class LoginResponse {
    private String token;
    private String email;
    private String NID;

    public LoginResponse() {
    }

    public LoginResponse(String token, String email, String NID) {
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

    public String getNID() {
        return NID;
    }

    public void setNID(String NID) {
        this.NID = NID;
    }
}
