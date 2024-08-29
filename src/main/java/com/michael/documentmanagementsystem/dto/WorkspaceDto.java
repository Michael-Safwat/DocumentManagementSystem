package com.michael.documentmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDto {
    private String id;
    private String name;
    private String path;
    private Long userNID;
}
