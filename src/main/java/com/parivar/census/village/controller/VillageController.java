package com.parivar.census.village.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.common.dto.PaginationRequest;
import com.parivar.census.village.dto.request.VillageRequest;
import com.parivar.census.village.dto.response.VillageResponse;
import com.parivar.census.village.service.VillageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.parivar.census.common.dto.PageResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/villages")
@RequiredArgsConstructor
public class VillageController {

    private final VillageService villageService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VillageResponse> createVillage(
            @Valid @RequestBody VillageRequest request) {

        log.info("Received request to create village with code: {}",
                request.getVillageCode());

        VillageResponse response = villageService.createVillage(request);

        return ApiResponse.<VillageResponse>builder()
                .success(true)
                .message("Village created successfully")
                .data(response)
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY','VIEWER')")
    public ApiResponse<PageResponse<VillageResponse>> getAllVillages(PaginationRequest request) {
        log.info("Received request to fetch all villages");
        return ApiResponse.success("Villages fetched successfully",villageService.getAllVillages(request));
    }
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY','VIEWER')")
    public ApiResponse<PageResponse<VillageResponse>> searchVillages(
            @RequestParam String keyword,
            PaginationRequest request) {

        log.info(
                "Received request to search villages with keyword {}",
                keyword);

        return ApiResponse.success(
                "Villages fetched successfully",
                villageService.searchVillages(
                        keyword,
                        request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY','VIEWER')")
    public ApiResponse<VillageResponse> getVillageById(
            @PathVariable Long id) {

        log.info("Received request to fetch village {}", id);

        VillageResponse response =
                villageService.getVillageById(id);

        return ApiResponse.<VillageResponse>builder()
                .success(true)
                .message("Village fetched successfully")
                .data(response)
                .build();
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ApiResponse<VillageResponse> updateVillage(
            @PathVariable Long id,
            @Valid @RequestBody VillageRequest request) {

        log.info("Received request to update village {}", id);

        VillageResponse response = villageService.updateVillage(id, request);

        return ApiResponse.<VillageResponse>builder()
                .success(true)
                .message("Village updated successfully")
                .data(response)
                .build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ApiResponse<Void> deleteVillage(@PathVariable Long id) {

        log.info("Received request to delete village {}", id);

        villageService.deleteVillage(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Village deleted successfully")
                .build();
    }
    @GetMapping("/district/{districtId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY','VIEWER')")
    public ApiResponse<List<VillageResponse>> getVillagesByDistrict(
            @PathVariable Long districtId) {

        log.info("Received request to fetch villages for district {}",
                districtId);

        List<VillageResponse> response =
                villageService.getVillagesByDistrict(districtId);

        return ApiResponse.<List<VillageResponse>>builder()
                .success(true)
                .message("Villages fetched successfully")
                .data(response)
                .build();
    }
}