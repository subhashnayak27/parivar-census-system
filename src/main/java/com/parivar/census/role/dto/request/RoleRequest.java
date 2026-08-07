package com.parivar.census.role.dto.request;

import com.parivar.census.role.enums.RoleName;
import lombok.Data;

@Data
public class RoleRequest {

    private RoleName roleName;

    private String description;

    private Boolean active;

}