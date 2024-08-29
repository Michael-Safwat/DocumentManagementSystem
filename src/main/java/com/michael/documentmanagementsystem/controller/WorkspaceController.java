package com.michael.documentmanagementsystem.controller;

import com.michael.documentmanagementsystem.dto.WorkspaceDto;
import com.michael.documentmanagementsystem.model.Workspace;
import com.michael.documentmanagementsystem.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.FileSystemException;
import java.util.List;

@RestController
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @PostMapping("/workspaces")
    public ResponseEntity<WorkspaceDto> createWorkspace(@RequestBody WorkspaceDto workspace) throws FileSystemException {
        return new ResponseEntity<>(workspaceService.createWorkspace(workspace),HttpStatus.CREATED);
    }

    @GetMapping("/workspaces/{NID}")
    public ResponseEntity<List<WorkspaceDto>> getWorkspacesByNID(@PathVariable Long NID) {
        return new ResponseEntity<>(workspaceService.getWorkspacesByNID(NID), HttpStatus.OK);
    }

    @PutMapping("/workspaces/{workspaceId}")
    public ResponseEntity<Boolean> updateWorkspace(@PathVariable String workspaceId, @RequestBody WorkspaceDto workspaceDto) {
        HttpStatus httpStatus;
        Boolean result = workspaceService.updateWorkspace(workspaceDto);

        if (result)
            httpStatus = HttpStatus.OK;
        else
            httpStatus = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(result, httpStatus);
    }

    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<HttpStatus> deleteWorkspace(@PathVariable String workspaceId) throws ChangeSetPersister.NotFoundException {
        workspaceService.deleteWorkspace(workspaceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
