package com.parivar.census.state.service;

import com.parivar.census.common.dto.PageResponse;
import com.parivar.census.common.dto.PaginationRequest;
import com.parivar.census.state.dto.request.StateRequest;
import com.parivar.census.state.dto.response.StateResponse;

public interface StateService {

    StateResponse createState(StateRequest request);
    StateResponse getStateById(Long id);
    PageResponse<StateResponse> getAllStates(PaginationRequest request);
    StateResponse updateState(Long id, StateRequest request);
    void deleteState(Long id);
}