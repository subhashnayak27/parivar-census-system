package com.parivar.census.member.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.member.dto.response.MemberUploadResponse;
import com.parivar.census.member.service.MemberUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberUploadController {

    private final MemberUploadService memberUploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MemberUploadResponse> uploadMembers(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ApiResponse.failure("Please select an Excel file.");
        }

        return ApiResponse.success(memberUploadService.uploadMembers(file));
    }

    @GetMapping("/template")
    public ResponseEntity<Resource> downloadTemplate() throws IOException {

        ClassPathResource resource =
                new ClassPathResource("templates/Member_Bulk_Upload_Template.xlsx");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Member_Bulk_Upload_Template.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}