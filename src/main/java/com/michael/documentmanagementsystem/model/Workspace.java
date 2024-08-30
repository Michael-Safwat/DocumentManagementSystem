package com.michael.documentmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

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
    private String path;
    private User user;
    private List<Document> documents;
    private boolean isDeleted;
}
