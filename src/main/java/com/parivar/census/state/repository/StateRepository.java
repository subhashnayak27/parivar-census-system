package com.parivar.census.state.repository;

import com.parivar.census.state.entity.State.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Long> {
    boolean existsByStateCode(String stateCode);
    boolean existsByStateName(String stateName);
    boolean existsByStateCodeAndIdNot(String stateCode, Long id);
    boolean existsByStateNameAndIdNot(String stateName, Long id);
    List<State> findByActiveTrue();
}