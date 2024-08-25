package com.michael.documentmanagementsystem.model;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
public class File {

    @Id
    @GeneratedValue
    private String id ;
    private String workspaceId;

    public File() {
        this.id = UUID.randomUUID().toString();
    }

    public File(String id, String workspaceId) {
        this.id = id;
        this.workspaceId = workspaceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }
}
