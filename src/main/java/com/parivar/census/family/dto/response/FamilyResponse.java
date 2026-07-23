package com.parivar.census.family.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FamilyResponse {

    private Long id;

    private String familyCode;

    private String familyHeadName;

    private String address;

    private String mobileNo;

    private String rationCardNo;

    private Long villageId;

    private String villageName;

    private Boolean active;
}