package com.michael.documentmanagementsystem.repository;

import com.michael.documentmanagementsystem.model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<com.michael.documentmanagementsystem.model.Document, String> {

    Document findByIdAndWorkspaceIdAndUserNID(String id, String workspaceId, Long userNID);
}
