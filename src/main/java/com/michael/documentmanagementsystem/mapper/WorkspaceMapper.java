package com.michael.documentmanagementsystem.mapper;

import com.michael.documentmanagementsystem.dto.WorkspaceDto;
import com.michael.documentmanagementsystem.model.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {
    @Mapping(ignore = true, target = "user.password")
    WorkspaceDto toDto(Workspace workspace);

    Workspace toEntity(WorkspaceDto workspaceDto);

    List<WorkspaceDto> ToDtos(List<Workspace> workspaces);
}
