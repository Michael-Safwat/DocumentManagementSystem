package com.michael.documentmanagementsystem.repository;

import com.michael.documentmanagementsystem.model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentRepository extends MongoRepository<com.michael.documentmanagementsystem.model.Document, String> {

    Document findByIdAndWorkspaceId(String id, String workspaceId);

    List<Document> findAllByWorkspaceId(String workspaceId);
}
