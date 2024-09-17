package com.michael.documentmanagementsystem.mapper;

import com.michael.documentmanagementsystem.dto.DocumentDTO;
import com.michael.documentmanagementsystem.model.Document;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentDTO toDto(Document document);

    List<DocumentDTO> toDtos(List<Document> documents);
}
