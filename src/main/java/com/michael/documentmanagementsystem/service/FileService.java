package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.dto.FileDto;
import com.michael.documentmanagementsystem.dto.WorkspaceDto;
import com.michael.documentmanagementsystem.mapper.FileMapper;
import com.michael.documentmanagementsystem.model.File;
import com.michael.documentmanagementsystem.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    public FileService(FileRepository fileRepository, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    public FileDto uploadFile(MultipartFile file, WorkspaceDto workspace) throws IOException {

        File savedFile = fileRepository.save(File.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .workspaceId(workspace.getId())
                        .userNID(workspace.getUserNID())
                        .path(workspace.getPath()+ java.io.File.separator+file.getOriginalFilename())
                        .isDeleted(false)
                        .build());

        file.transferTo(new java.io.File(savedFile.getPath()));

        return fileMapper.toDto(savedFile);
    }
}
