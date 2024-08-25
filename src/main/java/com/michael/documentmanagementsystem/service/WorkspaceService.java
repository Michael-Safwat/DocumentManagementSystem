package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.dto.WorkspaceRequest;
import com.michael.documentmanagementsystem.mapper.WorkspaceMapper;
import com.michael.documentmanagementsystem.model.Workspace;
import com.michael.documentmanagementsystem.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkspaceService {

    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    WorkspaceMapper workspaceMapper;

    public String createWorkspace(WorkspaceRequest workspaceRequest)
    {
        Workspace workspace = workspaceMapper.workspaceRequestToWorkspace(workspaceRequest);
        workspaceRepository.save(workspace);
        return "Success";
    }

    public List<Workspace> getWorkspacesByNID(Long NID) {
        return workspaceRepository.findAllByUserNID(NID);
    }

    public boolean updateWorkspace(WorkspaceRequest workspaceRequest)
    {
        Workspace workspace = workspaceRepository.findById(workspaceRequest.getId()).orElseThrow(IllegalStateException::new);

        workspace.setName(workspaceRequest.getName());
        workspace.setUserNID(workspaceRequest.getUserNID());
        workspaceRepository.save(workspace);
        return true;
    }

    public void deleteWorkspace(String workspaceId) {
         workspaceRepository.deleteById(workspaceId);
    }
}
