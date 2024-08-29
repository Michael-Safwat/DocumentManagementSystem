package com.michael.documentmanagementsystem.controller;

import com.michael.documentmanagementsystem.dto.FileDto;
import com.michael.documentmanagementsystem.dto.WorkspaceDto;
import com.michael.documentmanagementsystem.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {
    private final FileService fileService;
    public FileController(FileService fileService)
    {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    public ResponseEntity<FileDto> uploadFile(@RequestParam("file") MultipartFile file,
                                              @RequestPart("workspace") WorkspaceDto workspace) throws IOException {
        return new ResponseEntity<>(fileService.uploadFile(file,workspace), HttpStatus.OK);
    }
}
