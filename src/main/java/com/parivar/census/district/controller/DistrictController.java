package com.parivar.census.district.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.district.dto.request.DistrictRequest;
import com.parivar.census.district.dto.response.DistrictResponse;
import com.parivar.census.district.service.DistrictService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
@Slf4j
public class DistrictController {

    private final DistrictService districtService;

    @PostMapping
    public ApiResponse<DistrictResponse> createDistrict(@Valid @RequestBody DistrictRequest request) {

        DistrictResponse response = districtService.createDistrict(request);

        return ApiResponse.<DistrictResponse>builder()
                .success(true)
                .message("District created successfully")
                .data(response)
                .build();
    }
    @GetMapping("/{id}")
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
    public ApiResponse<List<DistrictResponse>> getAllDistricts() {

        log.info("Received request to fetch all districts");

        List<DistrictResponse> response =
                districtService.getAllDistricts();

        return ApiResponse.<List<DistrictResponse>>builder()
                .success(true)
                .message("Districts fetched successfully")
                .data(response)
                .build();
    }
    @PutMapping("/{id}")
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
    public ApiResponse<Void> deleteDistrict(@PathVariable Long id) {

        districtService.deleteDistrict(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("District deleted successfully")
                .build();
    }
    @GetMapping("/state/{stateId}")
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
}