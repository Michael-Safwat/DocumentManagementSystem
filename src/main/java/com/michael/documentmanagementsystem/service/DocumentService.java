package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.dto.DocumentDTO;
import com.michael.documentmanagementsystem.mapper.DocumentMapper;
import com.michael.documentmanagementsystem.model.Document;
import com.michael.documentmanagementsystem.repository.DocumentRepository;
import com.michael.documentmanagementsystem.service.util.UtilService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final UtilService utilService;

    public Pair<byte[], Pair<String, String>> downloadDocument(String did) throws IOException {
        Document document = utilService.isDocumentOwnerAndAvailable(documentRepository.findById(did));
        String filePath = document.getPath();
        return new Pair<>(Files.readAllBytes(new java.io.File(filePath).toPath()), new Pair<>(document.getType(), document.getName()));
    }

    public String previewDocument(String did) throws IOException {

        Document document = utilService.isDocumentOwnerAndAvailable(documentRepository.findById(did));
        String filePath = document.getPath();
        byte[] fileContent = Files.readAllBytes(new java.io.File(filePath).toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public void deleteDocument(String did) {

        Document document = utilService.isDocumentOwnerAndAvailable(documentRepository.findById(did));
        document.setDeleted(true);
        documentRepository.save(document);
    }

    public DocumentDTO updateDocument(String did, DocumentDTO updatedDocument) {
        Document document = utilService.isDocumentOwnerAndAvailable(documentRepository.findById(did));
        document.setName(updatedDocument.getName());
        documentRepository.save(document);
        return documentMapper.toDto(document);
    }
}
