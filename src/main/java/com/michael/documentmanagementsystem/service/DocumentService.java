package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.dto.DocumentDto;
import com.michael.documentmanagementsystem.mapper.DocumentMapper;
import com.michael.documentmanagementsystem.model.Document;
import com.michael.documentmanagementsystem.model.Workspace;
import com.michael.documentmanagementsystem.repository.DocumentRepository;
import com.michael.documentmanagementsystem.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final WorkspaceRepository workspaceRepository;
    private final DocumentRepository documentRepository;

    public Pair<byte[], Pair<String,String>> downloadDocument(String did) throws IOException {
        Document document = this.documentRepository.findById(did).orElse(null);
        String filePath = document.getPath();
        return new Pair<>(Files.readAllBytes(new java.io.File(filePath).toPath()), new Pair<>(document.getType(),document.getName()));
    }

    public String previewDocument(String did) throws IOException {

        Document document = this.documentRepository.findById(did).orElse(null);
        String filePath = document.getPath();
        byte[] fileContent  = Files.readAllBytes(new java.io.File(filePath).toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public boolean deleteDocument(String fid) {

        Document document = documentRepository.findById(fid).orElse(null);
        Workspace workspace = workspaceRepository.findById(document.getWorkspaceId()).orElse(null);

        workspace.getDocumentsIds().removeIf(documentId -> documentId.equals(document.getId()));
        document.setDeleted(true);
        workspaceRepository.save(workspace);
        documentRepository.save(document);
        return true;
    }

    public void updateDocument(String did, DocumentDto updatedDocument) {
        Document document = documentRepository.findById(did).orElse(null);
        document.setName(updatedDocument.getName());
        documentRepository.save(document);
    }
}
