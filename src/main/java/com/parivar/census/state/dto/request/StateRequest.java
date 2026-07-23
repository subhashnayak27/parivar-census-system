package com.parivar.census.state.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StateRequest {

    @NotBlank(message = "State code is required")
    private String stateCode;

    @NotBlank(message = "State name is required")
    private String stateName;
}