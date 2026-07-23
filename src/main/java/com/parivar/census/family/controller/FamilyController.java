package com.parivar.census.family.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.family.dto.request.FamilyRequest;
import com.parivar.census.family.dto.response.FamilyResponse;
import com.parivar.census.family.service.FamilyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/families")
@RequiredArgsConstructor
public class FamilyController {
    private final FamilyService familyService;

    @PostMapping
    public ApiResponse<FamilyResponse> createFamily(@Valid @RequestBody FamilyRequest request) {

        FamilyResponse response = familyService.createFamily(request);

        return ApiResponse.<FamilyResponse>builder()
                .success(true)
                .message("Family created successfully")
                .data(response)
                .build();
    }
}
