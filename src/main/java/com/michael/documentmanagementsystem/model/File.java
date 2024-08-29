package com.michael.documentmanagementsystem.model;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class File {

    @Id
    @GeneratedValue
    private String id ;
    private String workspaceId;
    private String name;
    private String type;
    private Long userNID;
    private String path;
    private boolean isDeleted;
}
