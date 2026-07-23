package com.parivar.census.village.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.village.dto.request.VillageRequest;
import com.parivar.census.village.dto.response.VillageResponse;
import com.parivar.census.village.service.VillageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/villages")
@RequiredArgsConstructor
public class VillageController {

    private final VillageService villageService;

    @PostMapping
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
}