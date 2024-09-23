package com.michael.documentmanagementsystem.service.util;

import com.michael.documentmanagementsystem.dto.WorkspaceDTO;
import com.michael.documentmanagementsystem.mapper.WorkspaceMapper;
import com.michael.documentmanagementsystem.model.Document;
import com.michael.documentmanagementsystem.model.User;
import com.michael.documentmanagementsystem.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.FileSystemException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final WorkspaceMapper workspaceMapper;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Long getNID() {
        Authentication authentication = getAuthentication();
        return ((User) authentication.getPrincipal()).getNID();
    }

    public void isAuthorized(Long NID)
    {
        if(!getNID().equals(NID))
        {
            AuthorizationDecision authorizationDecision = new AuthorizationDecision(false);
            throw new AuthorizationDeniedException("Not Authorized", authorizationDecision);
        }
    }

    public Workspace isWorkspaceOwnerAndAvailable(Optional<Workspace> workspace) {
        if (workspace.isEmpty() || workspace.get().isDeleted())
            throw new IllegalStateException("Workspace not found");

        if (!getNID().equals(workspace.get().getOwner())) {
            AuthorizationDecision authorizationDecision = new AuthorizationDecision(false);
            throw new AuthorizationDeniedException("Not Authorized", authorizationDecision);
        }
        return workspace.get();
    }

    public Document isDocumentOwnerAndAvailable(Optional<Document> document) {
        if (document.isEmpty() || document.get().isDeleted())
            throw new IllegalStateException("Document not found");

        if (!getNID().equals(document.get().getOwner())) {
            AuthorizationDecision authorizationDecision = new AuthorizationDecision(false);
            throw new AuthorizationDeniedException("Not Authorized", authorizationDecision);
        }
        return document.get();
    }

    public Workspace intializeWorkspace(WorkspaceDTO workspaceDto, String parentId) {
        //clean the workspace name
        workspaceDto.setName(workspaceDto.getName().replaceAll(" ", "_"));
        Workspace workspace = workspaceMapper.toEntity(workspaceDto);

        //create unique name for the local machine using date and time
        LocalDateTime date = LocalDateTime.now();
        String name = (workspaceDto.getName() + "_" + date).replaceAll(":", "_");
        workspace.setSavedName(name);

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
