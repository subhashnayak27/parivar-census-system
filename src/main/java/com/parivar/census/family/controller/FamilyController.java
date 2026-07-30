package com.parivar.census.family.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.family.dto.request.FamilyRequest;
import com.parivar.census.family.dto.response.FamilyResponse;
import com.parivar.census.family.service.FamilyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/families")
@RequiredArgsConstructor
@Slf4j
public class FamilyController {
    private final FamilyService familyService;

    @GetMapping("/{id}")
    public ApiResponse<FamilyResponse> getFamilyById(
            @PathVariable Long id) {
        log.info("Received request to fetch family with id {}", id);
        FamilyResponse response = familyService.getFamilyById(id);
        return ApiResponse.<FamilyResponse>builder()
                .success(true)
                .message("Family fetched successfully")
                .data(response)
                .build();
    }
    @GetMapping
    public ApiResponse<List<FamilyResponse>> getAllFamilies() {
        log.info("Received request to fetch all families");
        List<FamilyResponse> response = familyService.getAllFamilies();
        return ApiResponse.<List<FamilyResponse>>builder()
                .success(true)
                .message("Families fetched successfully")
                .data(response)
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<FamilyResponse> updateFamily(
            @PathVariable Long id,
            @Valid @RequestBody FamilyRequest request) {
        log.info("Received request to update family with id {}", id);
        FamilyResponse response = familyService.updateFamily(id, request);
        return ApiResponse.<FamilyResponse>builder()
                .success(true)
                .message("Family updated successfully")
                .data(response)
                .build();
    }
    @PostMapping
    public ApiResponse<FamilyResponse> createFamily(@Valid @RequestBody FamilyRequest request) {

        FamilyResponse response = familyService.createFamily(request);

        return ApiResponse.<FamilyResponse>builder()
                .success(true)
                .message("Family created successfully")
                .data(response)
                .build();
    }
    @GetMapping("/village/{villageId}")
    public ApiResponse<List<FamilyResponse>> getFamiliesByVillage(
            @PathVariable Long villageId) {

        log.info("Received request to fetch families for village {}",
                villageId);

        List<FamilyResponse> response =
                familyService.getFamiliesByVillage(villageId);

        return ApiResponse.<List<FamilyResponse>>builder()
                .success(true)
                .message("Families fetched successfully")
                .data(response)
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFamily(@PathVariable Long id) {

        log.info("Received request to delete family with id {}", id);

        familyService.deleteFamily(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Family deleted successfully")
                .build();
    }
}
