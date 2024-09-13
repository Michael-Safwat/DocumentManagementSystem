package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.dto.DocumentDto;
import com.michael.documentmanagementsystem.dto.WorkspaceDto;
import com.michael.documentmanagementsystem.mapper.DocumentMapper;
import com.michael.documentmanagementsystem.mapper.WorkspaceMapper;
import com.michael.documentmanagementsystem.model.Document;
import com.michael.documentmanagementsystem.model.User;
import com.michael.documentmanagementsystem.model.Workspace;
import com.michael.documentmanagementsystem.repository.DocumentRepository;
import com.michael.documentmanagementsystem.repository.UserRepository;
import com.michael.documentmanagementsystem.repository.WorkspaceRepository;
import com.michael.documentmanagementsystem.service.util.UtilService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private static final String PATH = "D:\\Programming\\My Projects\\Java Projects\\DocumentManagementSystem\\Workspaces";

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final UtilService utilService;

    public WorkspaceDto createWorkspace(WorkspaceDto workspaceDto, Long userNID) throws FileSystemException {

        Workspace workspace = utilService.intializeWorkspace(workspaceDto,"root");
        workspace.setUserNID(userNID);
        workspace.setPath(PATH + File.separator + userNID + File.separator + workspace.getSavedName());

        workspace = workspaceRepository.save(workspace);
        utilService.saveOnLocalMachine(PATH + File.separator + userNID,workspace.getSavedName());
        return workspaceMapper.toDto(workspace);
    }

    public WorkspaceDto createDirectory(String parentId,WorkspaceDto workspaceDto) throws FileSystemException {

        Workspace workspace = workspaceRepository.findById(parentId).orElse(null);

        Workspace directory = utilService.intializeWorkspace(workspaceDto,parentId);
        directory.setUserNID(workspace.getUserNID());
        directory.setPath(workspace.getPath() + File.separator + directory.getSavedName());

        directory = workspaceRepository.save(directory);
        utilService.saveOnLocalMachine(workspace.getPath(),directory.getSavedName());

        workspace.getDirectoriesIds().add(directory.getId());
        workspaceRepository.save(workspace);

        return workspaceMapper.toDto(directory);
    }

    public List<WorkspaceDto> getAllWorkspaces(String parentId) {
        List<Workspace> workspaces = workspaceRepository.findAllByParentId(parentId)
                .stream()
                .filter(workspace -> !workspace.isDeleted() && workspace.getParentId().equals(parentId))
                .toList();
        return workspaceMapper.ToDtos(workspaces);
    }

    public WorkspaceDto getWorkspacesById(String workspaceId) {

        Workspace workspace = workspaceRepository.findById(workspaceId).orElse(null);
        return workspaceMapper.toDto(workspace);
    }

    public Boolean updateWorkspace(String workspaceId, WorkspaceDto workspaceDto) {

        Workspace workspace = workspaceRepository.findById(workspaceId).orElse(null);
        workspace.setName(workspaceDto.getName().replaceAll(" ", "_"));
        workspace.setDescription(workspaceDto.getDescription());
        workspaceRepository.save(workspace);
        return true;
    }

    public boolean deleteWorkspace(String workspaceId) {

        Workspace workspace = workspaceRepository.findById(workspaceId).orElse(null);
        workspace.setDeleted(true);

        for(String DirectoryId: workspace.getDirectoriesIds())
            deleteWorkspace(DirectoryId);

        for (String documentId : workspace.getDocumentsIds()) {
            Document document = documentRepository.findById(documentId)
                    .orElseThrow(() -> new IllegalStateException("no such document found"));
            document.setDeleted(true);
            documentRepository.save(document);
        }
        workspaceRepository.save(workspace);
        return true;

    }

    public boolean uploadDocument(MultipartFile file, String workspaceId)
            throws IOException {

        Workspace workspace = workspaceRepository.findById(workspaceId).orElse(null);
        Document savedDocument = documentRepository.save(Document.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .workspaceId(workspaceId)
                .userNID(workspace.getUserNID())
                .path(workspace.getPath() + java.io.File.separator + file.getOriginalFilename())
                .isDeleted(false)
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")))
                .build());

        file.transferTo(new java.io.File(savedDocument.getPath()));

        workspace.getDocumentsIds().add(savedDocument.getId());
        workspaceRepository.save(workspace);
        return true;
    }

    public List<DocumentDto> getAllDocuments(String workspaceId) {
        List<Document> documents = documentRepository.findAllByWorkspaceId(workspaceId)
                .stream()
                .filter(document -> !document.isDeleted())
                .toList();
        return documentMapper.toDtos(documents);
    }
}
