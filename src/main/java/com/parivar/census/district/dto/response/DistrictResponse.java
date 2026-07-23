package com.parivar.census.district.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DistrictResponse {

    private Long id;

    private String districtCode;

    private String districtName;
    
    private String villageCode;

    private Long stateId;

    private String stateName;

    private Boolean active;
}