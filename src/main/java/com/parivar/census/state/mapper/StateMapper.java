package com.parivar.census.state.mapper;

import com.parivar.census.state.dto.request.StateRequest;
import com.parivar.census.state.dto.response.StateResponse;
import com.parivar.census.state.entity.State.State;

public class StateMapper {

    public static State toEntity(StateRequest request) {
        return State.builder()
                .stateCode(request.getStateCode())
                .stateName(request.getStateName())
                .build();
    }

    public static StateResponse toResponse(State state) {
        return StateResponse.builder()
                .id(state.getId())
                .stateCode(state.getStateCode())
                .stateName(state.getStateName())
                .active(state.getActive())
                .build();
    }
}