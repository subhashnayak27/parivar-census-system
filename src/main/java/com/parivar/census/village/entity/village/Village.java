package com.parivar.census.village.entity.village;

import com.parivar.census.common.entity.BaseEntity;
import com.parivar.census.district.entity.district.District;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "village")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Village extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "village_code", nullable = false, unique = true)
    private String villageCode;

    @Column(name = "village_name", nullable = false)
    private String villageName;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;


}