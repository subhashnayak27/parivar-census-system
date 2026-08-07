package com.parivar.census.village.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VillageResponse {

    private Long id;

    private String villageCode;

    private String postalCode;

    private String villageName;

    private Long districtId;

    private String districtName;

    private Long stateId;

    private String stateName;

    private Boolean active;
}