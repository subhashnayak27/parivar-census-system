package com.parivar.census.family.dto.request;

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
public class FamilyRequest {

  /*  @NotBlank(message = "Family Code is required")
    @Size(max = 20, message = "Family Code cannot exceed 20 characters")
    private String familyCode;
*/
    @NotBlank(message = "Family Head Name is required")
    @Size(max = 150, message = "Family Head Name cannot exceed 150 characters")
    private String familyHeadName;

    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Invalid Mobile Number"
    )
    private String mobileNo;

    @Size(max = 30, message = "Ration Card No cannot exceed 30 characters")
    private String rationCardNo;

    @NotNull(message = "Village Id is required")
    private Long villageId;
}