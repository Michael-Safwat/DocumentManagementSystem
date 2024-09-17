package com.michael.documentmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    private String id;
    private String parentId;
    private String name;
    private String type;
    private Long owner;
    private String createdAt;
}
