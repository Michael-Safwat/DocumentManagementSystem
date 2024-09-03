package com.michael.documentmanagementsystem.repository;

import com.michael.documentmanagementsystem.model.Workspace;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends MongoRepository<Workspace, String> {
    List<Workspace> findAllByUserNID(Long NID);
}
