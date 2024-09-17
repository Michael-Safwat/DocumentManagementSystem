package com.michael.documentmanagementsystem.dto;

import com.michael.documentmanagementsystem.validationgroups.WorkspaceCreation;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDTO {
    private String id;
    @NotBlank(message = "workspace name can't be empty", groups = WorkspaceCreation.class)
    private String name;
    private String description;
    private Long owner;
    private String path;
    private String parentId;
    private String createdAt;
}
