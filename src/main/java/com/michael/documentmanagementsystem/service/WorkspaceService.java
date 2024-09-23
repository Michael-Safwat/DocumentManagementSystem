package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.dto.DocumentDTO;
import com.michael.documentmanagementsystem.dto.WorkspaceDTO;
import com.michael.documentmanagementsystem.mapper.DocumentMapper;
import com.michael.documentmanagementsystem.mapper.WorkspaceMapper;
import com.michael.documentmanagementsystem.model.Document;
import com.michael.documentmanagementsystem.model.Workspace;
import com.michael.documentmanagementsystem.repository.DocumentRepository;
import com.michael.documentmanagementsystem.repository.WorkspaceRepository;
import com.michael.documentmanagementsystem.service.util.UtilService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
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

    public WorkspaceDTO createWorkspace(WorkspaceDTO workspaceDto) throws FileSystemException {

        Workspace workspace = utilService.intializeWorkspace(workspaceDto, "root");
        workspace.setPath(workspace.getName());
        Long userNID = utilService.getNID();
        workspace.setOwner(userNID);
        workspace.setLocalPath(PATH + File.separator + userNID + File.separator + workspace.getSavedName());

        workspace = workspaceRepository.save(workspace);
        utilService.saveOnLocalMachine(PATH + File.separator + userNID, workspace.getSavedName());
        return workspaceMapper.toDto(workspace);
    }

    public List<WorkspaceDTO> getAllWorkspaces(String parentId) {

        List<Workspace> workspaces = workspaceRepository.findAllByOwnerAndParentId(utilService.getNID(), parentId)
                .stream()
                .filter(workspace -> !workspace.isDeleted())
                .toList();
        return workspaceMapper.toDtos(workspaces);
    }

    public WorkspaceDTO getWorkspacesById(String workspaceId) {
        Workspace workspace = utilService.isWorkspaceOwnerAndAvailable(workspaceRepository.findById(workspaceId));
        return workspaceMapper.toDto(workspace);
    }

    public WorkspaceDTO updateWorkspace(String workspaceId, WorkspaceDTO workspaceDto) {

        Workspace workspace = utilService.isWorkspaceOwnerAndAvailable(workspaceRepository.findById(workspaceId));
        workspace.setName(workspaceDto.getName().replaceAll(" ", "_"));
        workspace.setDescription(workspaceDto.getDescription());
        workspaceRepository.save(workspace);
        return workspaceMapper.toDto(workspace);
    }

    public void deleteWorkspace(String workspaceId) {

        Workspace workspace = utilService.isWorkspaceOwnerAndAvailable(workspaceRepository.findById(workspaceId));
        workspace.setDeleted(true);

        List<Workspace> directories = workspaceRepository.findAllByParentId(workspaceId)
                .stream()
                .filter(directory-> !directory.isDeleted())
                .toList();
        for (Workspace directory : directories)
            deleteWorkspace(directory.getId());

        List<Document> documents = documentRepository.findAllByParentId(workspaceId);
        for (Document document : documents) {
            document.setDeleted(true);
        }
        documentRepository.saveAll(documents);
        workspaceRepository.save(workspace);
    }

    public WorkspaceDTO createDirectory(String parentId, WorkspaceDTO workspaceDto) throws FileSystemException {

        Workspace workspace = utilService.isWorkspaceOwnerAndAvailable(workspaceRepository.findById(parentId));

        Workspace directory = utilService.intializeWorkspace(workspaceDto, parentId);
        directory.setOwner(workspace.getOwner());
        directory.setLocalPath(workspace.getLocalPath() + File.separator + directory.getSavedName());
        directory.setPath(workspace.getPath() + File.separator + directory.getName());

        directory = workspaceRepository.save(directory);
        utilService.saveOnLocalMachine(workspace.getLocalPath(), directory.getSavedName());

        return workspaceMapper.toDto(directory);
    }

    public DocumentDTO uploadDocument(MultipartFile file, String workspaceId)
            throws IOException {

        Workspace workspace = utilService.isWorkspaceOwnerAndAvailable(workspaceRepository.findById(workspaceId));
        Document savedDocument = documentRepository.save(Document.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .parentId(workspaceId)
                .owner(workspace.getOwner())
                .path(workspace.getLocalPath() + java.io.File.separator + file.getOriginalFilename())
                .isDeleted(false)
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")))
                .build());

        file.transferTo(new java.io.File(savedDocument.getPath()));

        return documentMapper.toDto(savedDocument);
    }

    public List<DocumentDTO> getAllDocuments(String workspaceId) {
        utilService.isWorkspaceOwnerAndAvailable(workspaceRepository.findById(workspaceId));
        List<Document> documents = documentRepository.findAllByParentId(workspaceId)
                .stream()
                .filter(document -> !document.isDeleted())
                .toList();
        return documentMapper.toDtos(documents);
    }

    public Pair<List<WorkspaceDTO>, List<DocumentDTO>> search(String searchTerm, String parentID) {
        utilService.isWorkspaceOwnerAndAvailable(workspaceRepository.findById(parentID));
        List<Workspace> workspaces = workspaceRepository.findByNameContainingIgnoreCaseAndParentId(searchTerm, parentID)
                .stream()
                .filter(workspace -> !workspace.isDeleted())
                .toList();
        List<Document> documents = documentRepository.findByNameContainingIgnoreCaseAndParentIdOrTypeContainingIgnoreCaseAndParentId(searchTerm,parentID,searchTerm, parentID)
                .stream()
                .filter(document -> !document.isDeleted())
                .toList();

        return new Pair<>(workspaceMapper.toDtos(workspaces), documentMapper.toDtos(documents));
    }

    public List<WorkspaceDTO> searchWorkspaces(String searchTerm, Long owner) {
        utilService.isAuthorized(owner);
        List<Workspace> workspaces = workspaceRepository.findByNameContainingIgnoreCaseAndOwnerAndParentId(searchTerm, owner,"root")
                .stream()
                .filter(workspace -> !workspace.isDeleted())
                .toList();
        return workspaceMapper.toDtos(workspaces);
    }
}
