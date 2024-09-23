package com.michael.documentmanagementsystem.repository;

import com.michael.documentmanagementsystem.model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentRepository extends MongoRepository<com.michael.documentmanagementsystem.model.Document, String> {

    List<Document> findAllByParentId(String parentId);
    List<Document> findByNameContainingIgnoreCaseAndParentIdOrTypeContainingIgnoreCaseAndParentId(String name, String parentId1, String type, String parentId2);
}
