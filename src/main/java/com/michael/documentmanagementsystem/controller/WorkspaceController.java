package com.michael.documentmanagementsystem.controller;

import com.michael.documentmanagementsystem.dto.WorkspaceDto;
import com.michael.documentmanagementsystem.service.WorkspaceService;
import com.michael.documentmanagementsystem.validationgroups.WorkspaceCreation;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping("/workspaces/{nid}")
    public ResponseEntity<WorkspaceDto> createWorkspace(@RequestBody @Validated(WorkspaceCreation.class) WorkspaceDto workspace,
                                                        @PathVariable("nid") Long nid)
            throws Exception {
        return new ResponseEntity<>(workspaceService.createWorkspace(workspace, nid), HttpStatus.CREATED);
    }

    @GetMapping("/workspaces/{nid}")
    public ResponseEntity<List<WorkspaceDto>> getWorkspacesByNID(@PathVariable("nid") Long nid) {
        return new ResponseEntity<>(workspaceService.getWorkspacesByNID(nid), HttpStatus.OK);
    }

    @GetMapping("/workspaces/{nid}/{workspaceId}")
    public ResponseEntity<WorkspaceDto> getWorkspaceById(@PathVariable("workspaceId") String workspaceId)
    {
        return new ResponseEntity<>(workspaceService.getWorkspacesById(workspaceId),HttpStatus.OK);
    }

    @PutMapping("/workspaces/{nid}/{workspaceId}")
    public ResponseEntity<HttpStatus> updateWorkspace(@PathVariable String workspaceId, @RequestBody WorkspaceDto workspaceDto) {
        HttpStatus httpStatus;
        Boolean result = workspaceService.updateWorkspace(workspaceId,workspaceDto);

        if (result)
            httpStatus = HttpStatus.OK;
        else
            httpStatus = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/workspaces/{nid}/{workspaceId}")
    public ResponseEntity<HttpStatus> deleteWorkspace(@PathVariable("workspaceId") String workspaceId) {
        boolean result = workspaceService.deleteWorkspace(workspaceId);
        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/workspaces/{nid}/{workspaceId}/files")
    public ResponseEntity<HttpStatus> uploadDocument(@RequestParam("file") MultipartFile file,
                                                     @PathVariable("nid") Long nid,
                                                     @PathVariable("workspaceId") String workspaceId) throws IOException {

        boolean result = workspaceService.uploadDocument(file, nid, workspaceId);
        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Todo: get all files by NID
    /*@GetMapping("/workspaces/{nid}/{workspacesId}/files")
    public ResponseEntity<?> downloadFile(@PathVariable String fid) throws IOException {
        Pair<byte[], String> pair = workspaceService.downloadFile(fid);
        byte[] data = pair.a;
        String type = pair.b;

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(type))
                .body(data);
    }*/

    @GetMapping("/workspaces/{nid}/{workspaceId}/files/{fid}")
    public ResponseEntity<?> downloadDocument(@PathVariable("nid") Long nid,
                                              @PathVariable("workspaceId") String workspaceId,
                                              @PathVariable("fid") String fid)
            throws IOException {

        Pair<byte[], String> pair = workspaceService.downloadDocument(nid, workspaceId, fid);

        if (pair.a == null && pair.b == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        byte[] data = pair.a;
        String type = pair.b;

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(type))
                .body(data);
    }

    @DeleteMapping("/workspaces/{nid}/{workspaceId}/files/{fid}")
    public ResponseEntity<HttpStatus> deleteDocument(@PathVariable("nid") Long nid,
                                                     @PathVariable("workspaceId") String workspaceId,
                                                     @PathVariable("fid") String fid) {
        boolean result = workspaceService.deleteDocument(nid, workspaceId, fid);
        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
