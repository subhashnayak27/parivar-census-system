package com.parivar.census.dashboard.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.dashboard.dto.response.DashboardResponse;
import com.parivar.census.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    @GetMapping
    public ApiResponse<DashboardResponse> getDashboard() {
        return ApiResponse.<DashboardResponse>builder()
                .success(true)
                .message("Dashboard fetched successfully")
                .data(dashboardService.getDashboard())
                .build();
    }
}
