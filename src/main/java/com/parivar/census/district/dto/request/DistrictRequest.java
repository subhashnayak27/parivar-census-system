package com.parivar.census.district.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistrictRequest {

    @NotBlank(message = "District Code is required")
    private String districtCode;

    @NotBlank(message = "District Name is required")
    private String districtName;

    @NotNull(message = "State Id is required")
    private Long stateId;
}