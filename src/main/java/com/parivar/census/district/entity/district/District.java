package com.parivar.census.district.entity.district;

import com.parivar.census.common.entity.BaseEntity;
import com.parivar.census.state.entity.State.State;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "district")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class District extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "district_code", nullable = false, unique = true)
    private String districtCode;

    @Column(name = "district_name", nullable = false)
    private String districtName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", nullable = false)
    private State state;


}