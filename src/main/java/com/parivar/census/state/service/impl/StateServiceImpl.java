package com.parivar.census.state.service.impl;

import com.parivar.census.state.entity.State.State;
import com.parivar.census.state.repository.StateRepository.StateRepository;
import com.parivar.census.state.service.StateService.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final StateRepository repository;

    @Override
    public State save(State state) {
        return repository.save(state);
    }
}