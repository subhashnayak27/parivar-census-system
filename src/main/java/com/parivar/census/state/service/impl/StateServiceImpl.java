package com.parivar.census.state.service.impl;

import com.parivar.census.common.dto.PaginationRequest;
import com.parivar.census.common.service.CodeGeneratorService;
import com.parivar.census.common.util.PaginationUtil;
import com.parivar.census.exception.ResourceNotFoundException;
import com.parivar.census.state.dto.request.StateRequest;
import com.parivar.census.state.dto.response.StateResponse;
import com.parivar.census.exception.DuplicateResourceException;
import com.parivar.census.state.entity.State.State;
import com.parivar.census.state.repository.StateRepository;
import com.parivar.census.state.service.StateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import com.parivar.census.common.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.parivar.census.common.util.PageResponseUtil;
@Service
@Slf4j
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final StateRepository repository;
    private final CodeGeneratorService codeGeneratorService;
    @Override
    public StateResponse createState(StateRequest request) {

        log.info("Creating state with code {}", request.getStateCode());

        // Duplicate State Code
        if (repository.existsByStateCode(request.getStateCode())) {
            throw new DuplicateResourceException(
                    "State code already exists : " + request.getStateCode());
        }

        // Duplicate State Name
        if (repository.existsByStateName(request.getStateName())) {
            throw new DuplicateResourceException(
                    "State name already exists : " + request.getStateName());
        }

        State state = State.builder()
                .stateCode(request.getStateCode())
                .stateName(request.getStateName())
                .active(true)
                .build();

        State savedState = repository.save(state);

        log.info("State created successfully with id {}", savedState.getId());

        return mapToResponse(savedState);
    }

    @Override
    public StateResponse getStateById(Long id) {

        log.info("Fetching state with id {}", id);

        State state = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("State not found with id {}", id);
                    return new ResourceNotFoundException(
                            "State not found with id : " + id);
                });

        if (!state.getActive()) {
            throw new ResourceNotFoundException(
                    "State not found with id : " + id);
        }

        return mapToResponse(state);
    }

    @Override
    public PageResponse<StateResponse> getAllStates(PaginationRequest request) {

        log.info("Fetching states. Page: {}, Size: {}",
                request.getPage(),
                request.getSize());

        Pageable pageable =
                PaginationUtil.getPageable(request);

        Page<State> statePage =
                repository.findByActiveTrue(pageable);

        List<StateResponse> response =
                statePage.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return PageResponseUtil.of(statePage, response);
    }

    @Override
    public StateResponse updateState(Long id, StateRequest request) {

        log.info("Updating state {}", id);

        State state = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "State not found with id : " + id));

        if (repository.existsByStateCodeAndIdNot(
                request.getStateCode(), id)) {

            throw new DuplicateResourceException(
                    "State code already exists : " + request.getStateCode());
        }

        if (repository.existsByStateNameAndIdNot(
                request.getStateName(), id)) {

            throw new DuplicateResourceException(
                    "State name already exists : " + request.getStateName());
        }

        state.setStateCode(
                codeGeneratorService.generateStateCode()
        );
        state.setStateName(request.getStateName());

        State updated = repository.save(state);

        log.info("State updated successfully {}", updated.getId());

        return mapToResponse(updated);
    }

    @Override
    public void deleteState(Long id) {

        log.info("Deleting state {}", id);

        State state = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "State not found with id : " + id));

        state.setActive(false);

        repository.save(state);

        log.info("State deleted successfully {}", id);
    }

    private StateResponse mapToResponse(State state) {

        return StateResponse.builder()
                .id(state.getId())
                .stateCode(state.getStateCode())
                .stateName(state.getStateName())
                .active(state.getActive())
                .build();
    }

    @Override
    public PageResponse<StateResponse> searchStates(
            String keyword,
            PaginationRequest request) {

        log.info("Searching states with keyword {}", keyword);

        Pageable pageable = PaginationUtil.getPageable(request);

        Page<State> statePage;

        if (keyword == null || keyword.isBlank()) {

            statePage = repository.findByActiveTrue(pageable);

        } else {

            statePage =
                    repository.findByActiveTrueAndStateNameContainingIgnoreCaseOrActiveTrueAndStateCodeContainingIgnoreCase(
                            keyword,
                            keyword,
                            pageable);
        }

        List<StateResponse> response =
                statePage.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return PageResponseUtil.of(statePage, response);
    }
}