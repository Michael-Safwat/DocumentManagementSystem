package com.michael.documentmanagementsystem.model;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@org.springframework.data.mongodb.core.mapping.Document
@Builder
public class Document {

    @Id
    @GeneratedValue
    private String id;
    private String workspaceId;
    private String name;
    private String type;
    private Long userNID;
    private String path;
    private boolean isDeleted;
}
