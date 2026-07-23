package com.parivar.census.district.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.district.dto.request.DistrictRequest;
import com.parivar.census.district.dto.response.DistrictResponse;
import com.parivar.census.district.service.DistrictService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
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
}