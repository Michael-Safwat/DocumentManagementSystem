package com.michael.documentmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private String id ;
    private String workspaceId;
    private String name;
    private String type;
    private Long userNID;
    private String path;
    private boolean isDeleted;
}
