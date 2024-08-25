package com.michael.documentmanagementsystem.repository;

import com.michael.documentmanagementsystem.model.Workspace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends MongoRepository<Workspace, String> {
    List<Workspace> findAllByUserNID(Long NID);
}
