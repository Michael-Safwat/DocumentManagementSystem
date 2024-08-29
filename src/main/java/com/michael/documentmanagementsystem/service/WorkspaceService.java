package com.michael.documentmanagementsystem.service;

import com.michael.documentmanagementsystem.dto.WorkspaceDto;
import com.michael.documentmanagementsystem.mapper.WorkspaceMapper;
import com.michael.documentmanagementsystem.model.Workspace;
import com.michael.documentmanagementsystem.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.FileSystemException;
import java.util.List;

@Service
public class WorkspaceService {

    private static final String PATH = "D:\\Programming\\My Projects\\Java Projects\\DocumentManagementSystem\\Workspaces";

    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    WorkspaceMapper workspaceMapper;

    public WorkspaceDto createWorkspace(WorkspaceDto workspaceDto) throws FileSystemException {

        Workspace workspace = workspaceMapper.dtoToWorkspace(workspaceDto);

        String NID = workspace.getUserNID().toString();

        File userWorkspaces = new File(PATH + File.separator + NID);
        File newFile = new File(userWorkspaces, workspace.getName());
        boolean result = newFile.mkdirs();

        if (!result)
            throw new FileSystemException("error creating directory");

        workspace.setPath(PATH + File.separator + NID + File.separator + workspace.getName());
        Workspace savedWorkspace = workspaceRepository.save(workspace);
        workspaceDto.setPath(workspace.getPath());
        workspaceDto.setId(savedWorkspace.getId());

        return workspaceDto;
    }

    public List<WorkspaceDto> getWorkspacesByNID(Long NID) {
        List<Workspace> workspaces = workspaceRepository.findAllByUserNID(NID);
        return workspaceMapper.workspacesToDtos(workspaces);
    }

    public boolean updateWorkspace(WorkspaceDto workspaceDto) {
        Workspace workspace = workspaceRepository.findById(workspaceDto.getId()).orElseThrow(IllegalStateException::new);

        workspace.setName(workspaceDto.getName());
        workspace.setUserNID(workspaceDto.getUserNID());
        workspaceRepository.save(workspace);
        return true;
    }

    public void deleteWorkspace(String workspaceId) throws ChangeSetPersister.NotFoundException {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        if (workspace.isDeleted())
            throw new ChangeSetPersister.NotFoundException();
        else
            workspace.setDeleted(true);
    }
}
