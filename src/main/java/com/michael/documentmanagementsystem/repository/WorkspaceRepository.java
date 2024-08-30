package com.michael.documentmanagementsystem.repository;

import com.michael.documentmanagementsystem.model.Workspace;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends MongoRepository<Workspace, String> {
    Workspace findByIdAndUser_NID(String id, Long nid);

    List<Workspace> findAllByUserNID(Long NID);
}
