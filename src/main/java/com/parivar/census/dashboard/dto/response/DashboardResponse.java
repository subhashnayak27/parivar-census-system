package com.parivar.census.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {

        private Long totalStates;
        private Long totalDistricts;
        private Long totalVillages;
        private Long totalFamilies;
        private Long totalMembers;
        private Long activeMembers;

        private Long maleMembers;
        private Long femaleMembers;
        private Long otherMembers;

        private Long marriedMembers;
        private Long singleMembers;
    }
