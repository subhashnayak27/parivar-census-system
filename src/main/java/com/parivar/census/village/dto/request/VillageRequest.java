package com.parivar.census.village.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VillageRequest {

    @NotBlank(message = "Village Name is required")
    @Size(max = 100, message = "Village Name cannot exceed 100 characters")
    private String villageName;

    @NotBlank(message = "Postal Code is required")
    @Pattern(
            regexp = "^[0-9]{6}$",
            message = "Postal Code must be exactly 6 digits"
    )
    private String postalCode;

    @NotNull(message = "District is required")
    private Long districtId;
}