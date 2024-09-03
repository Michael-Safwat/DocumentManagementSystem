package com.michael.documentmanagementsystem.dto;

import com.michael.documentmanagementsystem.validationgroups.WorkspaceCreation;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDto {
    private String id;
    @NotBlank(message = "workspace name can't be empty", groups = WorkspaceCreation.class)
    private String name;
    private String description;
    private Long userNID;
    private List<String> documentsIds;
    private String createdAt;
}
