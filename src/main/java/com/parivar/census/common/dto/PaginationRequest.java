package com.parivar.census.common.dto;

import lombok.Data;

@Data
public class PaginationRequest {

    private int page = 0;

    private int size = 10;

    private String sortBy = "id";

    private String direction = "asc";
}