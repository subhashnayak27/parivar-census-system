package com.parivar.census.state.entity.State;

import com.parivar.census.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "state")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class State extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state_code", nullable = false, unique = true, length = 10)
    private String stateCode;

    @Column(name = "state_name", nullable = false, length = 100)
    private String stateName;
    private Boolean active;
}