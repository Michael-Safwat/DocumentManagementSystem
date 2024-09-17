package com.michael.documentmanagementsystem.repository;

import com.michael.documentmanagementsystem.model.Workspace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends MongoRepository<Workspace, String> {

    List<Workspace> findAllByParentId(String parentId);
    List<Workspace> findAllByOwnerAndParentId(Long owner, String parentId);
    List<Workspace> findByNameContainingIgnoreCaseAndParentId(String name, String parentId);
}
