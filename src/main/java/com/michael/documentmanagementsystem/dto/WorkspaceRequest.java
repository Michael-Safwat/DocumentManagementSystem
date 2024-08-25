package com.michael.documentmanagementsystem.dto;

public class WorkspaceRequest {
    private String id;
    private String name;
    private Long userNID;


    public WorkspaceRequest() {}

    public WorkspaceRequest(String id, String name, Long userNID) {
        this.id = id;
        this.name = name;
        this.userNID = userNID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserNID() {
        return userNID;
    }

    public void setUserNID(Long userNID) {
        this.userNID = userNID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
