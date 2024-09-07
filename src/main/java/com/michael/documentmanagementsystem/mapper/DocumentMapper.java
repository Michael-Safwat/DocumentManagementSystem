package com.michael.documentmanagementsystem.mapper;

import com.michael.documentmanagementsystem.dto.DocumentDto;
import com.michael.documentmanagementsystem.model.Document;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    List<DocumentDto> toDtos(List<Document> documents);
}
