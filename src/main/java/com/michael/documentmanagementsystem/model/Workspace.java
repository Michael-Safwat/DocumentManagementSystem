package com.michael.documentmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@AllArgsConstructor
@NoArgsConstructor
@org.springframework.data.mongodb.core.mapping.Document
public class Workspace {

    @Id
    @GeneratedValue
    private String id;
    @Indexed(unique = true)
    private String name;
    private String savedName;
    private String description;
    private String path;
    private String localPath;
    private Long owner;
    private String parentId;
    private boolean isDeleted;
    private String createdAt;
}
