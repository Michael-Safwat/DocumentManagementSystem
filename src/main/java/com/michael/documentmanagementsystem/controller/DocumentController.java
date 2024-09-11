package com.michael.documentmanagementsystem.controller;

import com.michael.documentmanagementsystem.dto.DocumentDto;
import com.michael.documentmanagementsystem.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/{did}")
    @PreAuthorize("@utilService.isDocumentOwnerAndAvailable(authentication,#did)")
    public ResponseEntity<?> downloadDocument(@PathVariable("did") String did) throws IOException {

        Pair<byte[], Pair<String,String>> pair = documentService.downloadDocument(did);

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

    @DeleteMapping("/{did}")
    @PreAuthorize("@utilService.isDocumentOwnerAndAvailable(authentication,#did)")
    public ResponseEntity<HttpStatus> deleteDocument(@PathVariable("did") String did) {

        boolean result = documentService.deleteDocument(did);
        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{did}/preview")
    @PreAuthorize("@utilService.isDocumentOwnerAndAvailable(authentication,#did)")
    public ResponseEntity<String> previewDocument(@PathVariable("did")String did) throws IOException {
        return new ResponseEntity<>(documentService.previewDocument(did),HttpStatus.OK);
    }

    @PutMapping("/{did}")
    @PreAuthorize("@utilService.isDocumentOwnerAndAvailable(authentication,#did)")
    public ResponseEntity<HttpStatus> updateDocument(@PathVariable("did") String did,
                                                     @RequestBody DocumentDto updatedDocument){
        documentService.updateDocument(did,updatedDocument);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
