package com.michael.documentmanagementsystem.model;

import jakarta.persistence.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;


@Document
public class Workspace {

    @Id
    @GeneratedValue
    private String id;
    @Indexed(unique = true)
    private String name;
    private Long userNID;
    private List<File> files;

    public Workspace() {
        this.id = UUID.randomUUID().toString();
    }

    public Workspace(String id, String name, Long NID, List<File> files) {
        this.id = id;
        this.name = name;
        this.userNID = NID;
        this.files = files;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
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

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
