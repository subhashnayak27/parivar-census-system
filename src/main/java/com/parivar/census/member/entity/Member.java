package com.parivar.census.member.entity;

import com.parivar.census.common.entity.BaseEntity;
import com.parivar.census.family.entity.Family;
import com.parivar.census.member.enums.Gender;
import com.parivar.census.member.enums.MaritalStatus;
import com.parivar.census.member.enums.RelationshipType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
    @SequenceGenerator(name = "member_seq", sequenceName = "member_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String memberCode;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    private LocalTime birthTime;

    @Column(nullable = false)
    @Builder.Default
    private Boolean alive = true;

    private LocalDate dateOfDeath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationshipType relationship;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaritalStatus maritalStatus;

    @Column(length = 10)
    private String mobileNo;

    @Column(length = 12, unique = true)
    private String aadhaarNo;

    @Column(length = 100)
    private String occupation;

    @Column(length = 100)
    private String education;

    @Column(name = "gotra", length = 100)
    private String gotra;

    @Column(name = "pata", length = 100)
    private String pata;

    @Column(name = "kuldevi", length = 150)
    private String kuldevi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;
}