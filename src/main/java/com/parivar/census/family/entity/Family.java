package com.parivar.census.family.entity;

import com.parivar.census.common.entity.BaseEntity;
import com.parivar.census.village.entity.village.Village;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "family")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Family extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "family_code", nullable = false, unique = true, length = 20)
    private String familyCode;

    @Column(name = "family_head_name", nullable = false, length = 150)
    private String familyHeadName;

    @Column(length = 500)
    private String address;

    @Column(name = "mobile_no", length = 10)
    private String mobileNo;

    @Column(name = "ration_card_no", length = 30)
    private String rationCardNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_id", nullable = false)
    private Village village;

}