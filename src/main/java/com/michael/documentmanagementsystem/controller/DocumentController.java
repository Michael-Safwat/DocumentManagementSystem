package com.michael.documentmanagementsystem.controller;

import com.michael.documentmanagementsystem.dto.DocumentDTO;
import com.michael.documentmanagementsystem.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/{did}")
    public ResponseEntity<?> downloadDocument(@PathVariable("did") String did) throws IOException {

        Pair<byte[], Pair<String, String>> pair = documentService.downloadDocument(did);

        if (pair.a == null && pair.b == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        byte[] data = pair.a;
        String type = pair.b.a;
        String fileName = pair.b.b;

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(type))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(data);
    }

    @GetMapping("/{did}/preview")
    public ResponseEntity<String> previewDocument(@PathVariable("did") String did) throws IOException {
        return new ResponseEntity<>(documentService.previewDocument(did), HttpStatus.OK);
    }

    @DeleteMapping("/{did}")
    public ResponseEntity<HttpStatus> deleteDocument(@PathVariable("did") String did) {

        documentService.deleteDocument(did);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{did}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable("did") String did,
                                                      @RequestBody DocumentDTO updatedDocument) {
        return new ResponseEntity<>(documentService.updateDocument(did, updatedDocument), HttpStatus.OK);
    }
}
