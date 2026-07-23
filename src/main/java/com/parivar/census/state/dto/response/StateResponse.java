package com.parivar.census.state.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StateResponse {

    private Long id;
    private String stateCode;
    private String stateName;
    private Boolean active;
}