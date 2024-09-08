package com.michael.documentmanagementsystem.controller;

import com.michael.documentmanagementsystem.dto.DocumentDto;
import com.michael.documentmanagementsystem.dto.WorkspaceDto;
import com.michael.documentmanagementsystem.service.WorkspaceService;
import com.michael.documentmanagementsystem.validationgroups.WorkspaceCreation;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping("/workspaces")
    public ResponseEntity<WorkspaceDto> createWorkspace(@RequestBody @Validated(WorkspaceCreation.class) WorkspaceDto workspace,
                                                        Authentication authentication)
            throws Exception {
        return new ResponseEntity<>(workspaceService.createWorkspace(workspace, authentication), HttpStatus.CREATED);
    }

    @GetMapping("/workspaces")
    public ResponseEntity<List<WorkspaceDto>> getWorkspacesByNID(Authentication authentication) {
        return new ResponseEntity<>(workspaceService.getWorkspacesByNID(authentication), HttpStatus.OK);
    }

    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<WorkspaceDto> getWorkspaceById(@PathVariable("workspaceId") String workspaceId,
                                                         Authentication authentication) {
        return new ResponseEntity<>(workspaceService.getWorkspacesById(workspaceId, authentication), HttpStatus.OK);
    }

    @PutMapping("/workspaces/{workspaceId}")
    public ResponseEntity<HttpStatus> updateWorkspace(@PathVariable String workspaceId,
                                                      @RequestBody WorkspaceDto workspaceDto,
                                                      Authentication authentication) {
        HttpStatus httpStatus;
        Boolean result = workspaceService.updateWorkspace(workspaceId, workspaceDto, authentication);

        if (result)
            httpStatus = HttpStatus.OK;
        else
            httpStatus = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<HttpStatus> deleteWorkspace(@PathVariable("workspaceId") String workspaceId,
                                                      Authentication authentication) {
        boolean result = workspaceService.deleteWorkspace(workspaceId, authentication);
        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/workspaces/{workspaceId}/files")
    public ResponseEntity<HttpStatus> uploadDocument(@RequestParam("file") MultipartFile file,
                                                     @PathVariable("workspaceId") String workspaceId,
                                                     Authentication authentication) throws IOException {

        boolean result = workspaceService.uploadDocument(file, workspaceId, authentication);
        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/workspaces/{workspacesId}/files")
    public ResponseEntity<List<DocumentDto>> getAllDocuments(@PathVariable String workspacesId,
                                                             Authentication authentication) {
        return new ResponseEntity<>(workspaceService.getAllDocuments(workspacesId, authentication), HttpStatus.OK);
    }

    @GetMapping("/workspaces/{workspaceId}/files/{fid}")
    public ResponseEntity<?> downloadDocument(@PathVariable("workspaceId") String workspaceId,
                                              @PathVariable("fid") String fid,
                                              Authentication authentication)
            throws IOException {

        Pair<byte[], String> pair = workspaceService.downloadDocument(workspaceId, fid, authentication);

        if (pair.a == null && pair.b == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        byte[] data = pair.a;
        String type = pair.b;

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(type))
                .body(data);
    }

    @GetMapping("/workspaces/{workspaceId}/filesPreview/{fid}")
    public ResponseEntity<String> previewDocument(@PathVariable("workspaceId") String workspaceId,
                                             @PathVariable("fid")String fid,
                                             Authentication authentication) throws IOException {

        return new ResponseEntity<>(workspaceService.previewDocument(workspaceId,fid,authentication),HttpStatus.OK);
    }

    @DeleteMapping("/workspaces/{workspaceId}/files/{fid}")
    public ResponseEntity<HttpStatus> deleteDocument(@PathVariable("workspaceId") String workspaceId,
                                                     @PathVariable("fid") String fid,
                                                     Authentication authentication) {
        boolean result = workspaceService.deleteDocument(workspaceId, fid, authentication);
        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
