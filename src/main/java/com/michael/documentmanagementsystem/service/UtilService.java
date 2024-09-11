package com.michael.documentmanagementsystem.service;

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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final WorkspaceRepository workspaceRepository;
    private final DocumentRepository documentRepository;

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
}
