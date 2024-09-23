package com.michael.documentmanagementsystem.controller;

import com.michael.documentmanagementsystem.dto.DocumentDTO;
import com.michael.documentmanagementsystem.dto.WorkspaceDTO;
import com.michael.documentmanagementsystem.service.WorkspaceService;
import com.michael.documentmanagementsystem.validationgroups.WorkspaceCreation;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping()
    public ResponseEntity<WorkspaceDTO> createWorkspace(@RequestBody @Validated(WorkspaceCreation.class) WorkspaceDTO workspace) throws Exception {

        return new ResponseEntity<>(workspaceService.createWorkspace(workspace), HttpStatus.CREATED);
    }

    @GetMapping("/all/{parentId}")
    public ResponseEntity<List<WorkspaceDTO>> getAllWorkspaces(@PathVariable String parentId) {
        return new ResponseEntity<>(workspaceService.getAllWorkspaces(parentId),HttpStatus.OK);
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceDTO> getWorkspaceById(@PathVariable("workspaceId") String workspaceId) {
        return new ResponseEntity<>(workspaceService.getWorkspacesById(workspaceId), HttpStatus.OK);
    }

    @PutMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceDTO> updateWorkspace(@PathVariable String workspaceId,
                                                        @RequestBody WorkspaceDTO workspaceDto) {
        return new ResponseEntity<>(workspaceService.updateWorkspace(workspaceId, workspaceDto), HttpStatus.OK);
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<HttpStatus> deleteWorkspace(@PathVariable("workspaceId") String workspaceId) {

        workspaceService.deleteWorkspace(workspaceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/directories/{parentId}")
    public ResponseEntity<WorkspaceDTO> createDirectory(@PathVariable("parentId") String parentID,
                                                        @RequestBody WorkspaceDTO workspaceDto) throws FileSystemException {
        return new ResponseEntity<>(workspaceService.createDirectory(parentID, workspaceDto), HttpStatus.OK);
    }

    @PostMapping("/{workspaceId}/documents")
    public ResponseEntity<DocumentDTO> uploadDocument(@RequestParam("file") MultipartFile file,
                                                      @PathVariable("workspaceId") String workspaceId) throws IOException {
        return new ResponseEntity<>(workspaceService.uploadDocument(file, workspaceId), HttpStatus.OK);
    }

    @GetMapping("/{workspacesId}/documents")
    public ResponseEntity<List<DocumentDTO>> getAllDocuments(@PathVariable String workspacesId) {
        return new ResponseEntity<>(workspaceService.getAllDocuments(workspacesId), HttpStatus.OK);
    }

    @GetMapping("/search/{searchTerm}/{owner}")
    public ResponseEntity<List<WorkspaceDTO>> searchWorkspaces(@PathVariable String searchTerm,@PathVariable Long owner)
    {
        return new ResponseEntity<>(workspaceService.searchWorkspaces(searchTerm, owner), HttpStatus.OK);
    }

    @GetMapping("/search/content/{searchTerm}/{parentID}")
    public ResponseEntity<Pair<List<WorkspaceDTO>, List<DocumentDTO>>> searchWorkspace(@PathVariable("searchTerm") String searchTerm,
                                                                                       @PathVariable("parentID") String parentID) {
        return new ResponseEntity<>(workspaceService.search(searchTerm, parentID), HttpStatus.OK);
    }
}
