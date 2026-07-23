package com.parivar.census.district.service.impl;

import com.parivar.census.district.dto.request.DistrictRequest;
import com.parivar.census.district.dto.response.DistrictResponse;
import com.parivar.census.district.entity.district.District;
import com.parivar.census.district.repository.DistrictRepository;
import com.parivar.census.district.service.DistrictService;
import com.parivar.census.exception.DuplicateResourceException;
import com.parivar.census.exception.ResourceNotFoundException;
import com.parivar.census.state.entity.State.State;
import com.parivar.census.state.repository.StateRepository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    private static final Logger log =
            LoggerFactory.getLogger(DistrictServiceImpl.class);
    private final DistrictRepository districtRepository;
    private final StateRepository stateRepository;

    @Override
    public DistrictResponse createDistrict(DistrictRequest request) {
        log.info("Received request to create district with code: {}",
                request.getDistrictCode());
        // Find State
        State state = stateRepository.findById(request.getStateId())
                .orElseThrow(() -> {
                    log.warn("State not found with id: {}", request.getStateId());

                    return new ResourceNotFoundException(
                            "State not found with id: " + request.getStateId());
                });

        // Create District Entity
        District district = District.builder()
                .districtCode(request.getDistrictCode())
                .districtName(request.getDistrictName())
                .state(state)
                .build();

        // Save
        log.info("Creating district: {}", request.getDistrictName());
        if (districtRepository.existsByDistrictCode(request.getDistrictCode())) {
        log.warn("District with code {} already exists", request.getDistrictCode());
            throw new DuplicateResourceException(
                    "District code already exists : {}" + request.getDistrictCode());
        }

        District savedDistrict = districtRepository.save(district);
        log.info("District created with id : {}",  savedDistrict.getId());
        // Convert to Response DTO
        return DistrictResponse.builder()
                .id(savedDistrict.getId())
                .districtCode(savedDistrict.getDistrictCode())
                .districtName(savedDistrict.getDistrictName())
                .stateId(state.getId())
                .stateName(state.getStateName())
                .active(savedDistrict.getActive())
                .build();
    }
}