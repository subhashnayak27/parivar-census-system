package com.parivar.census.common.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

    @Parameter(description = "Page number (starts from 0)", example = "0")
    @Min(0)
    private Integer page = 0;

    @Parameter(description = "Page size", example = "10")
    @Min(1)
    private Integer size = 10;

    @Parameter(description = "Sort field", example = "id")
    private String sortBy = "id";

    @Parameter(description = "Sort direction (asc/desc)", example = "asc")
    private String direction = "asc";
}