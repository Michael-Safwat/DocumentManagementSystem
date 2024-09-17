package com.michael.documentmanagementsystem.mapper;

import com.michael.documentmanagementsystem.dto.WorkspaceDTO;
import com.michael.documentmanagementsystem.model.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {
    @Mapping(ignore = true, target = "user.password")
    WorkspaceDTO toDto(Workspace workspace);

    Workspace toEntity(WorkspaceDTO workspaceDto);

    List<WorkspaceDTO> ToDtos(List<Workspace> workspaces);
}
