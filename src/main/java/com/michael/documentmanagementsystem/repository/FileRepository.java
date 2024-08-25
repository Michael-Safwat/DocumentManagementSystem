package com.michael.documentmanagementsystem.repository;

import com.michael.documentmanagementsystem.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File,String> {
}
