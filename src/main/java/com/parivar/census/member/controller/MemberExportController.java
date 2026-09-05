package com.parivar.census.member.controller;

import com.parivar.census.member.service.MemberExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberExportController {

    private final MemberExportService memberExportService;

    @GetMapping("/export")
    public ResponseEntity<Resource> exportMembers(@RequestParam(required = false) Long familyId) {
        return exportMembersWithIds(null, familyId);
    }

    @PostMapping("/export")
    public ResponseEntity<Resource> exportMembers(@RequestBody(required = false) Map<String, List<Long>> requestBody,
                                                @RequestParam(required = false) Long familyId) {
        List<Long> memberIds = requestBody == null ? null : requestBody.get("memberIds");
        return exportMembersWithIds(memberIds, familyId);
    }

    private ResponseEntity<Resource> exportMembersWithIds(List<Long> memberIds, Long familyId) {
        ByteArrayResource resource =
                new ByteArrayResource(
                        memberExportService.exportMembers(familyId, memberIds));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Members.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}