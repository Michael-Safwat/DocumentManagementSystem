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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private static final String PATH = "D:\\Programming\\My Projects\\Java Projects\\DocumentManagementSystem\\Workspaces";

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;

    public WorkspaceDto createWorkspace(WorkspaceDto workspaceDto,Authentication authentication) throws FileSystemException {

        Long nid = (((User)authentication.getPrincipal())) .getNID();
        //replace all spaces and replace them with underscores(not good with the machine)
        workspaceDto.setName(workspaceDto.getName().replaceAll(" ", "_"));
        Workspace workspace = workspaceMapper.toEntity(workspaceDto);
        User user = userRepository.findAllByNID(nid);

        if (user == null)
            throw new UsernameNotFoundException("No user with such NID");

        //create local name using time and date concatenation
        LocalDateTime date = LocalDateTime.now();
        String name = workspaceDto.getName() + "_" + date;
        name = name.replaceAll(":", "_");

        workspace.setSavedName(name);
        workspace.setPath(PATH + File.separator + nid + File.separator + workspace.getSavedName());
        workspace.setDocumentsIds(Collections.emptyList());
        workspace.setUserNID(nid);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        workspace.setCreatedAt(date.format(formatter));

        Workspace savedWorkspace = workspaceRepository.save(workspace);
        File userWorkspaces = new File(PATH + File.separator + nid);
        File newFile = new File(userWorkspaces, name);
        boolean result = newFile.mkdirs();

        if (!result)
            throw new FileSystemException("error creating directory");

        workspaceDto = workspaceMapper.toDto(savedWorkspace);

        return workspaceDto;
    }

    public List<WorkspaceDto> getWorkspacesByNID(Authentication authentication) {
        List<Workspace> workspaces = workspaceRepository.findAllByUserNID(((User) authentication.getPrincipal()).getNID());
        List<Workspace> result = workspaces.stream().filter(workspace -> !workspace.isDeleted()).toList();

        return workspaceMapper.ToDtos(result);
    }

    public WorkspaceDto getWorkspacesById(String workspaceId, Authentication authentication) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalStateException("no such workspace found"));
        if (workspace.getUserNID().equals(((User) authentication.getPrincipal()).getNID()))
            return workspaceMapper.toDto(workspace);
        else {
            AuthorizationDecision authorizationDecision = new AuthorizationDecision(false);
            throw new AuthorizationDeniedException("Not Authorized", authorizationDecision);
        }
    }

    public Boolean updateWorkspace(String workspaceId, WorkspaceDto workspaceDto, Authentication authentication) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if (workspace.isEmpty() || workspace.get().isDeleted())
            return false;
        if (!workspace.get().getUserNID().equals(((User) authentication.getPrincipal()).getNID())) {
            AuthorizationDecision authorizationDecision = new AuthorizationDecision(false);
            throw new AuthorizationDeniedException("Not Authorized", authorizationDecision);
        }
        workspace.get().setName(workspaceDto.getName().replaceAll(" ", "_"));
        workspace.get().setDescription(workspaceDto.getDescription());
        workspaceRepository.save(workspace.get());
        return true;
    }

    public boolean deleteWorkspace(String workspaceId, Authentication authentication) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if (workspace.isEmpty())
            return false;
        else if (!workspace.get().getUserNID().equals(((User) authentication.getPrincipal()).getNID())) {
            AuthorizationDecision authorizationDecision = new AuthorizationDecision(false);
            throw new AuthorizationDeniedException("Not Authorized", authorizationDecision);
        } else {
            workspace.get().setDeleted(true);
            workspace.get().setName(workspace.get().getSavedName());
            for (String documentId : workspace.get().getDocumentsIds()) {
                Document document = documentRepository.findById(documentId)
                        .orElseThrow(() -> new IllegalStateException("no such document found"));
                document.setDeleted(true);
                documentRepository.save(document);
            }
            workspaceRepository.save(workspace.get());
            return true;
        }

    }

    public boolean uploadDocument(MultipartFile file,
                                  String workspaceId,
                                  Authentication authentication)
            throws IOException {

        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);

        if (workspace.isEmpty() || workspace.get().isDeleted())
            throw new IllegalStateException("no such workspace or user");

        else if (!workspace.get().getUserNID().equals(((User) authentication.getPrincipal()).getNID())) {
            AuthorizationDecision authorizationDecision = new AuthorizationDecision(false);
            throw new AuthorizationDeniedException("Not Authorized", authorizationDecision);
        }

        Document savedDocument = documentRepository.save(Document.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .workspaceId(workspaceId)
                .userNID(((User) authentication.getPrincipal()).getNID())
                .path(workspace.get().getPath() + java.io.File.separator + file.getOriginalFilename())
                .isDeleted(false)
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                .build());

        file.transferTo(new java.io.File(savedDocument.getPath()));

        workspace.get().getDocumentsIds().add(savedDocument.getId());
        workspaceRepository.save(workspace.get());
        return true;
    }

    public Pair<byte[], String> downloadDocument(String workspaceId,
                                                 String fid,
                                                 Authentication authentication)
            throws IOException {
        com.michael.documentmanagementsystem.model.Document document
                = this.documentRepository.findByIdAndWorkspaceId(fid, workspaceId);

        if (document != null && !(document.getUserNID().equals(((User) authentication.getPrincipal()).getNID()))) {
            AuthorizationDecision authorizationDecision = new AuthorizationDecision(false);
            throw new AuthorizationDeniedException("Not Authorized", authorizationDecision);
        }

        if (document == null || document.isDeleted())
            return new Pair<>(null, null);
        else {
            String filePath = document.getPath();
            return new Pair<>(Files.readAllBytes(new java.io.File(filePath).toPath()), document.getType());
        }

    }

    public boolean deleteDocument(String workspaceId, String fid, Authentication authentication) {
        Document document = documentRepository.findByIdAndWorkspaceId(fid, workspaceId);

        if (document != null && !(document.getUserNID().equals(((User) authentication.getPrincipal()).getNID()))) {
            AuthorizationDecision authorizationDecision = new AuthorizationDecision(false);
            throw new AuthorizationDeniedException("Not Authorized", authorizationDecision);
        }

        if (document == null || document.isDeleted())
            return false;

        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);

        if (workspace.isEmpty())
            return false;

        workspace.get().getDocumentsIds().removeIf(documentId -> documentId.equals(document.getId()));

        document.setDeleted(true);
        workspaceRepository.save(workspace.get());
        documentRepository.save(document);
        return true;
    }
}
