package com.parivar.census.member.controller;

import com.parivar.census.member.service.MemberExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberExportController {

    private final MemberExportService memberExportService;

    @GetMapping("/export")
    public ResponseEntity<Resource> exportMembers() {

        ByteArrayResource resource =
                new ByteArrayResource(
                        memberExportService.exportMembers());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Members.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}