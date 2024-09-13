package com.michael.documentmanagementsystem.service.util;

import com.michael.documentmanagementsystem.dto.WorkspaceDto;
import com.michael.documentmanagementsystem.mapper.WorkspaceMapper;
import com.michael.documentmanagementsystem.model.Document;
import com.michael.documentmanagementsystem.model.User;
import com.michael.documentmanagementsystem.model.Workspace;
import com.michael.documentmanagementsystem.repository.DocumentRepository;
import com.michael.documentmanagementsystem.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.FileSystemException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final WorkspaceRepository workspaceRepository;
    private final DocumentRepository documentRepository;
    private final WorkspaceMapper workspaceMapper;

    public Long getNID(Authentication authentication)
    {
        return ((User)authentication.getPrincipal()).getNID();
    }

    public boolean isWorkspaceOwnerAndAvailable( Authentication authentication,String workspaceId)
    {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);

        if(workspace.isEmpty() || workspace.get().isDeleted())
            throw new IllegalStateException("Workspace not found");

        if(!getNID(authentication).equals(workspace.get().getUserNID()))
        {
            AuthorizationDecision authorizationDecision = new AuthorizationDecision(false);
            throw new AuthorizationDeniedException("Not Authorized", authorizationDecision);
        }
        return true;
    }

    public boolean isDocumentOwnerAndAvailable(Authentication authentication,String documentId)
    {
        Optional<Document> document = documentRepository.findById(documentId);

        if(document.isEmpty() || document.get().isDeleted())
            throw new IllegalStateException("Document not found");

        if (!getNID(authentication).equals(document.get().getUserNID()))
        {
            AuthorizationDecision authorizationDecision = new AuthorizationDecision(false);
            throw new AuthorizationDeniedException("Not Authorized", authorizationDecision);
        }
        return true;
    }

    public Workspace intializeWorkspace(WorkspaceDto workspaceDto, String parentId)
    {
        //clean the workspace name
        workspaceDto.setName(workspaceDto.getName().replaceAll(" ", "_"));
        Workspace workspace = workspaceMapper.toEntity(workspaceDto);

        //create unique name for the local machine using date and time
        LocalDateTime date = LocalDateTime.now();
        String name = (workspaceDto.getName() + "_" + date).replaceAll(":", "_");
        workspace.setSavedName(name);

        workspace.setDocumentsIds(Collections.emptyList());
        workspace.setDirectoriesIds(Collections.emptyList());

        workspace.setParentId(parentId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        workspace.setCreatedAt(date.format(formatter));

        return workspace;
    }

    public void saveOnLocalMachine(String path, String directoryName) throws FileSystemException {

        File userWorkspaces = new File(path);
        File newFile = new File(userWorkspaces, directoryName);
        boolean result = newFile.mkdirs();

        if (!result)
            throw new FileSystemException("error creating directory");
    }
}
