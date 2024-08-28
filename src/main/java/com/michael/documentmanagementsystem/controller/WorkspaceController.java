package com.michael.documentmanagementsystem.controller;

import com.michael.documentmanagementsystem.dto.WorkspaceRequest;
import com.michael.documentmanagementsystem.model.Workspace;
import com.michael.documentmanagementsystem.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @PostMapping("/workspaces")
    public ResponseEntity<String> createWorkspace(@RequestBody WorkspaceRequest workspace) {
        return new ResponseEntity<>(workspaceService.createWorkspace(workspace), HttpStatus.CREATED);
    }

    @GetMapping("/workspaces/{NID}")
    public ResponseEntity<List<Workspace>> getWorkspacesByNID(@PathVariable Long NID) {
        return new ResponseEntity<>(workspaceService.getWorkspacesByNID(NID), HttpStatus.OK);
    }

    @PutMapping("/workspaces/{workspaceId}")
    public ResponseEntity<Boolean> updateWorkspace(@PathVariable String workspaceId, @RequestBody WorkspaceRequest workspaceRequest) {
        HttpStatus httpStatus;
        Boolean result = workspaceService.updateWorkspace(workspaceRequest);

        if (result)
            httpStatus = HttpStatus.OK;
        else
            httpStatus = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(result, httpStatus);
    }

    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<HttpStatus> deleteWorkspace(@PathVariable String workspaceId) {
        workspaceService.deleteWorkspace(workspaceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
