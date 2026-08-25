package com.parivar.census.district.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.common.dto.PageResponse;
import com.parivar.census.district.dto.request.DistrictRequest;
import com.parivar.census.district.dto.response.DistrictResponse;
import com.parivar.census.district.service.DistrictService;
import com.parivar.census.state.dto.response.StateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.parivar.census.common.dto.PaginationRequest;
import java.util.List;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
@Slf4j
public class DistrictController {

    private final DistrictService districtService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY')")
    public ApiResponse<DistrictResponse> createDistrict(@Valid @RequestBody DistrictRequest request) {

        DistrictResponse response = districtService.createDistrict(request);

        return ApiResponse.<DistrictResponse>builder()
                .success(true)
                .message("District created successfully")
                .data(response)
                .build();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY','VIEWER')")
    public ApiResponse<DistrictResponse> getDistrictById(
            @PathVariable Long id) {

        log.info("Received request to fetch district {}", id);

        DistrictResponse response = districtService.getDistrictById(id);

        return ApiResponse.<DistrictResponse>builder()
                .success(true)
                .message("District fetched successfully")
                .data(response)
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY','VIEWER')")
    public ApiResponse<PageResponse<DistrictResponse>> getAllDistricts(
            @ParameterObject @ModelAttribute PaginationRequest request) {

        return ApiResponse.success(
                "Districts fetched successfully",
                districtService.getAllDistricts(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ApiResponse<DistrictResponse> updateDistrict(
            @PathVariable Long id,
            @Valid @RequestBody DistrictRequest request) {

        log.info("Received request to update district {}", id);

        DistrictResponse response =
                districtService.updateDistrict(id, request);

        return ApiResponse.<DistrictResponse>builder()
                .success(true)
                .message("District updated successfully")
                .data(response)
                .build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<Void> deleteDistrict(@PathVariable Long id) {

        districtService.deleteDistrict(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("District deleted successfully")
                .build();
    }
    @GetMapping("/state/{stateId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY','VIEWER')")
    public ApiResponse<List<DistrictResponse>> getDistrictsByState(
            @PathVariable Long stateId) {

        List<DistrictResponse> response =
                districtService.getDistrictsByState(stateId);

        return ApiResponse.<List<DistrictResponse>>builder()
                .success(true)
                .message("Districts fetched successfully")
                .data(response)
                .build();
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY','VIEWER')")
    @GetMapping("/search")
    public ApiResponse<PageResponse<DistrictResponse>> searchDistrict(
            @RequestParam String keyword,
            @ModelAttribute @ParameterObject PaginationRequest request) {

        return ApiResponse.success(
                "District fetched successfully",
                districtService.searchDistrict(keyword, request));
    }
}