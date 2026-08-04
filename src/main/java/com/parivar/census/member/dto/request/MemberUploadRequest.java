package com.parivar.census.member.dto.request;

import com.parivar.census.member.enums.Gender;
import com.parivar.census.member.enums.MaritalStatus;
import com.parivar.census.member.enums.RelationshipType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberUploadRequest {

    private String familyCode;

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

    private String gotra;

    private String pata;

    private String kuldevi;
}