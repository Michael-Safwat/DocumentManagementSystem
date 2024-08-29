package com.michael.documentmanagementsystem.mapper;

import com.michael.documentmanagementsystem.dto.FileDto;
import com.michael.documentmanagementsystem.model.File;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileDto toDto(File file);
}
