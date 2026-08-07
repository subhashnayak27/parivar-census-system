package com.parivar.census.role.dto.response;

import com.parivar.census.role.enums.RoleName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse {

    private Long id;

    private RoleName roleName;

    private String description;

    private Boolean active;

}