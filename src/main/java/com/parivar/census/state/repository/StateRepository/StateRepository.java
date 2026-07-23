package com.parivar.census.state.repository.StateRepository;

import com.parivar.census.state.entity.State.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {

}