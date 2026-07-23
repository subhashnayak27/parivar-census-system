package com.parivar.census.member.dto.request;

import com.parivar.census.member.enums.Gender;
import com.parivar.census.member.enums.MaritalStatus;
import com.parivar.census.member.enums.RelationshipType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequest {

    @NotBlank(message = "Member Code is required")
    @Size(max = 20, message = "Member Code cannot exceed 20 characters")
    private String memberCode;

    @NotBlank(message = "First Name is required")
    @Size(max = 100, message = "First Name cannot exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last Name cannot exceed 100 characters")
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of Birth is required")
    private LocalDate dateOfBirth;

    @Builder.Default
    private Boolean alive = true;

    private LocalDate dateOfDeath;

    @NotNull(message = "Relationship is required")
    private RelationshipType relationship;

    @NotNull(message = "Marital Status is required")
    private MaritalStatus maritalStatus;

    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Mobile number must be 10 digits"
    )
    private String mobileNo;

    @Pattern(
            regexp = "^[0-9]{12}$",
            message = "Aadhaar number must be 12 digits"
    )
    private String aadhaarNo;

    @Size(max = 100, message = "Occupation cannot exceed 100 characters")
    private String occupation;

    @Size(max = 100, message = "Education cannot exceed 100 characters")
    private String education;

    @NotNull(message = "Family Id is required")
    private Long familyId;
}