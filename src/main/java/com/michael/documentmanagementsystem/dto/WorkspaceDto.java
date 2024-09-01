package com.michael.documentmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDto {
    private String id;
    private String name;
    private UserDto user;
    private List<DocumentDto> documents;
}
