package com.parivar.census.user.dto.request;

import lombok.Data;

@Data
public class UserRequest {

    private String username;

    private String password;

    private String fullName;

    private String email;

    private String mobileNo;

    private Long roleId;
}