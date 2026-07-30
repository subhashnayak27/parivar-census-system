package com.parivar.census.state.service;

import com.parivar.census.state.dto.request.StateRequest;
import com.parivar.census.state.dto.response.StateResponse;

import java.util.List;

public interface StateService {

    StateResponse createState(StateRequest request);

    StateResponse getStateById(Long id);

    List<StateResponse> getAllStates();

    StateResponse updateState(Long id, StateRequest request);

    void deleteState(Long id);
}