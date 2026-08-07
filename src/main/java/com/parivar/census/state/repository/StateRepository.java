package com.parivar.census.state.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.parivar.census.state.entity.State.State;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StateRepository extends JpaRepository<State, Long> {
    boolean existsByStateCode(String stateCode);
    boolean existsByStateName(String stateName);
    boolean existsByStateCodeAndIdNot(String stateCode, Long id);
    boolean existsByStateNameAndIdNot(String stateName, Long id);
    Page<State> findByActiveTrue(Pageable pageable);
}