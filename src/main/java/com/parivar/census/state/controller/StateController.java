package com.parivar.census.state.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.district.dto.request.DistrictRequest;
import com.parivar.census.district.dto.response.DistrictResponse;
import com.parivar.census.state.dto.request.StateRequest;
import com.parivar.census.state.dto.response.StateResponse;
import com.parivar.census.state.service.StateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/states")
@RequiredArgsConstructor
@Slf4j
public class StateController {

    private final StateService stateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<StateResponse> createState(
            @Valid @RequestBody StateRequest request) {

        log.info("Received request to create state with code: {}",
                request.getStateCode());

        StateResponse response = stateService.createState(request);

        return ApiResponse.<StateResponse>builder()
                .success(true)
                .message("State created successfully")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<StateResponse> getStateById(
            @PathVariable Long id) {

        log.info("Received request to fetch state {}", id);

        StateResponse response = stateService.getStateById(id);

        return ApiResponse.<StateResponse>builder()
                .success(true)
                .message("State fetched successfully")
                .data(response)
                .build();
    }
    @PutMapping("/{id}")
    public ApiResponse<StateResponse> updateState(
            @PathVariable Long id,
            @Valid @RequestBody StateRequest request) {

        log.info("Received request to update State {}", id);

        StateResponse response =
                stateService.updateState(id, request);

        return ApiResponse.<StateResponse>builder()
                .success(true)
                .message("State updated successfully")
                .data(response)
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteState(@PathVariable Long id) {

        stateService.deleteState(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("State deleted successfully")
                .build();
    }
}