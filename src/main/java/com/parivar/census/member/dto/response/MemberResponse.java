package com.parivar.census.member.dto.response;

import com.parivar.census.member.enums.Gender;
import com.parivar.census.member.enums.MaritalStatus;
import com.parivar.census.member.enums.RelationshipType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MemberResponse {

    private Long id;

    private String memberCode;

    private String firstName;

    private String lastName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private Boolean alive;

    private LocalDate dateOfDeath;

    private RelationshipType relationship;

    private MaritalStatus maritalStatus;

    private String mobileNo;

    private String aadhaarNo;

    private String occupation;

    private String education;

    private Long familyId;

    private String familyCode;

    private String familyHeadName;

    private Boolean active;
}