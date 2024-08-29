package com.michael.documentmanagementsystem.mapper;

import com.michael.documentmanagementsystem.dto.WorkspaceDto;
import com.michael.documentmanagementsystem.model.Workspace;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {
    Workspace dtoToWorkspace(WorkspaceDto workspaceDto);
    List<WorkspaceDto> workspacesToDtos(List<Workspace> workspaces);
}
