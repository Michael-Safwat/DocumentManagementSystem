package com.michael.documentmanagementsystem.mapper;

import com.michael.documentmanagementsystem.dto.WorkspaceRequest;
import com.michael.documentmanagementsystem.model.Workspace;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {
    Workspace workspaceRequestToWorkspace(WorkspaceRequest workspaceRequest);
}
