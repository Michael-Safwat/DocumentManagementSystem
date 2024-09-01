package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.dto.WorkspaceDto;
import com.michael.documentmanagementsystem.mapper.WorkspaceMapper;
import com.michael.documentmanagementsystem.model.Document;
import com.michael.documentmanagementsystem.model.User;
import com.michael.documentmanagementsystem.model.Workspace;
import com.michael.documentmanagementsystem.repository.DocumentRepository;
import com.michael.documentmanagementsystem.repository.UserRepository;
import com.michael.documentmanagementsystem.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private static final String PATH = "D:\\Programming\\My Projects\\Java Projects\\DocumentManagementSystem\\Workspaces";

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;

    public WorkspaceDto createWorkspace(WorkspaceDto workspaceDto, Long nid) throws FileSystemException {

        Workspace workspace = workspaceMapper.toEntity(workspaceDto);
        User user = userRepository.findAllByNID(nid);

        if (user == null)
            throw new UsernameNotFoundException("No user with such NID");

        workspace.setPath(PATH + File.separator + nid + File.separator + workspace.getName());
        workspace.setDocuments(Collections.emptyList());
        workspace.setUser(user);


        Workspace savedWorkspace = workspaceRepository.save(workspace);
        File userWorkspaces = new File(PATH + File.separator + nid);
        File newFile = new File(userWorkspaces, workspace.getName());
        boolean result = newFile.mkdirs();

        if (!result)
            throw new FileSystemException("error creating directory");

        workspaceDto = workspaceMapper.toDto(savedWorkspace);

        return workspaceDto;
    }

    public List<WorkspaceDto> getWorkspacesByNID(Long NID) {
        List<Workspace> workspaces = workspaceRepository.findAllByUserNID(NID);
        List<Workspace> result = workspaces.stream().filter(workspace -> !workspace.isDeleted()).toList();

        for (Workspace workspace : result)
            workspace.getDocuments().removeIf(Document::isDeleted);

        return workspaceMapper.ToDtos(result);
    }

    //todo: update workspace
    /*public boolean updateWorkspace(WorkspaceDto workspaceDto) {
        Workspace workspace = workspaceRepository.findById(workspaceDto.getId()).orElseThrow(IllegalStateException::new);

        workspace.setName(workspaceDto.getName());
        workspace.setUserNID(workspaceDto.getUserNID());
        workspaceRepository.save(workspace);
        return true;
    }*/

    public boolean deleteWorkspace(Long nid, String workspaceId) {
        Workspace workspace = workspaceRepository.findByIdAndUser_NID(workspaceId, nid);
        if (workspace == null)
            return false;
        else {
            workspace.setDeleted(true);
            for (Document document : workspace.getDocuments()) {
                documentRepository.findByIdAndWorkspaceIdAndUserNID(document.getId(), workspaceId, nid).setDeleted(true);
                document.setDeleted(true);
            }
            workspaceRepository.save(workspace);
            return true;
        }
    }

    public boolean uploadDocument(MultipartFile file, Long nid, String workspaceId) throws IOException {

        Workspace workspace = workspaceRepository.findByIdAndUser_NID(workspaceId, nid);

        if (workspace == null || workspace.isDeleted())
            throw new IllegalStateException("no such workspace or user");

        Document savedDocument = documentRepository.save(Document.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .workspaceId(workspaceId)
                .userNID(nid)
                .path(workspace.getPath() + java.io.File.separator + file.getOriginalFilename())
                .isDeleted(false)
                .build());

        file.transferTo(new java.io.File(savedDocument.getPath()));

        workspace.getDocuments().add(savedDocument);
        workspaceRepository.save(workspace);

        return true;
    }

    public Pair<byte[], String> downloadDocument(Long nid,
                                                 String workspaceId,
                                                 String fid)
            throws IOException {
        com.michael.documentmanagementsystem.model.Document document
                = this.documentRepository.findByIdAndWorkspaceIdAndUserNID(fid, workspaceId, nid);

        if (document == null || document.isDeleted())
            return new Pair<>(null, null);
        else {
            String filePath = document.getPath();
            return new Pair<>(Files.readAllBytes(new java.io.File(filePath).toPath()), document.getType());
        }

    }

    public boolean deleteDocument(Long nid, String workspaceId, String fid) {
        Document document = documentRepository.findByIdAndWorkspaceIdAndUserNID(fid, workspaceId, nid);

        if (document == null || document.isDeleted())
            return false;

        Workspace workspace = workspaceRepository.findByIdAndUser_NID(workspaceId, nid);

        for (Document d : workspace.getDocuments()) {
            if (d.getId().equals(document.getId()))
                d.setDeleted(true);
        }

        document.setDeleted(true);
        workspaceRepository.save(workspace);
        documentRepository.save(document);
        return true;
    }
}
