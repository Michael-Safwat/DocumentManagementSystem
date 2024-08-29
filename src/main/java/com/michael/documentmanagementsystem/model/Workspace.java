package com.michael.documentmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Workspace {

    @Id
    @GeneratedValue
    private String id;
    @Indexed(unique = true)
    private String name;
    private String path;
    private Long userNID;
    private List<File> files;
    private boolean isDeleted;
}
