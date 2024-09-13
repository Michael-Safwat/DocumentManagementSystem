package com.michael.documentmanagementsystem.controller;

import com.michael.documentmanagementsystem.dto.DocumentDto;
import com.michael.documentmanagementsystem.dto.WorkspaceDto;
import com.michael.documentmanagementsystem.service.util.UtilService;
import com.michael.documentmanagementsystem.service.WorkspaceService;
import com.michael.documentmanagementsystem.validationgroups.WorkspaceCreation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private final UtilService utilService;

    @PostMapping()
    public ResponseEntity<WorkspaceDto> createWorkspace(@RequestBody @Validated(WorkspaceCreation.class) WorkspaceDto workspace,
                                                        Authentication authentication) throws Exception {

        return new ResponseEntity<>(workspaceService.createWorkspace(workspace,utilService.getNID(authentication)), HttpStatus.CREATED);
    }

    @GetMapping("/all/{parentId}")
    public ResponseEntity<List<WorkspaceDto>> getAllWorkspaces(@PathVariable String parentId) {
        return new ResponseEntity<>(workspaceService.getAllWorkspaces(parentId), HttpStatus.OK);
    }

    @GetMapping("/{workspaceId}")
    @PreAuthorize("@utilService.isWorkspaceOwnerAndAvailable(authentication,#workspaceId)")
    public ResponseEntity<WorkspaceDto> getWorkspaceById(@PathVariable("workspaceId") String workspaceId) {
        return new ResponseEntity<>(workspaceService.getWorkspacesById(workspaceId), HttpStatus.OK);
    }

    @PutMapping("/{workspaceId}")
    @PreAuthorize("@utilService.isWorkspaceOwnerAndAvailable(authentication,#workspaceId)")
    public ResponseEntity<HttpStatus> updateWorkspace(@PathVariable String workspaceId,
                                                      @RequestBody WorkspaceDto workspaceDto) {
        HttpStatus httpStatus;
        Boolean result = workspaceService.updateWorkspace(workspaceId, workspaceDto);

        if (result)
            httpStatus = HttpStatus.OK;
        else
            httpStatus = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/{workspaceId}")
    @PreAuthorize("@utilService.isWorkspaceOwnerAndAvailable(authentication,#workspaceId)")
    public ResponseEntity<HttpStatus> deleteWorkspace(@PathVariable("workspaceId") String workspaceId) {
        boolean result = workspaceService.deleteWorkspace(workspaceId);
        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/directories/{parentId}")
    @PreAuthorize("@utilService.isWorkspaceOwnerAndAvailable(authentication,#parentID)")
    public ResponseEntity<WorkspaceDto> createDirectory(@PathVariable("parentId") String parentID,
                                            @RequestBody WorkspaceDto workspaceDto) throws FileSystemException {
        return new ResponseEntity<>(workspaceService.createDirectory(parentID,workspaceDto),HttpStatus.OK);
    }

    @PostMapping("/{workspaceId}/documents")
    @PreAuthorize("@utilService.isWorkspaceOwnerAndAvailable(authentication,#workspaceId)")
    public ResponseEntity<HttpStatus> uploadDocument(@RequestParam("file") MultipartFile file,
                                                     @PathVariable("workspaceId") String workspaceId) throws IOException {

        boolean result = workspaceService.uploadDocument(file, workspaceId);
        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{workspacesId}/documents")
    @PreAuthorize("@utilService.isWorkspaceOwnerAndAvailable(authentication,#workspacesId)")
    public ResponseEntity<List<DocumentDto>> getAllDocuments(@PathVariable String workspacesId) {
        return new ResponseEntity<>(workspaceService.getAllDocuments(workspacesId), HttpStatus.OK);
    }
}
