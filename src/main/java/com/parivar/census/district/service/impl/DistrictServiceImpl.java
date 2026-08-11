package com.parivar.census.district.service.impl;

import com.parivar.census.common.dto.PageResponse;
import com.parivar.census.common.dto.PaginationRequest;
import com.parivar.census.common.util.PageResponseUtil;
import com.parivar.census.common.util.PaginationUtil;
import com.parivar.census.district.dto.request.DistrictRequest;
import com.parivar.census.district.dto.response.DistrictResponse;
import com.parivar.census.district.entity.district.District;
import com.parivar.census.district.repository.DistrictRepository;
import com.parivar.census.district.service.DistrictService;
import com.parivar.census.exception.DuplicateResourceException;
import com.parivar.census.exception.ResourceNotFoundException;
import com.parivar.census.state.entity.State.State;
import com.parivar.census.state.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    private static final Logger log =
            LoggerFactory.getLogger(DistrictServiceImpl.class);
    private final DistrictRepository districtRepository;
    private final StateRepository stateRepository;

    @Override
    public DistrictResponse getDistrictById(Long id) {

        log.info("Fetching district with id {}", id);

        District district = districtRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("District not found with id {}", id);

                    return new ResourceNotFoundException(
                            "District not found with id : " + id);
                });

        if (!district.getActive()) {
            throw new ResourceNotFoundException(
                    "District not found with id : " + id);
        }

        return mapToResponse(district);
    }


    @Override
    public PageResponse<DistrictResponse> getAllDistricts(
            PaginationRequest request) {

        log.info("Fetching districts. Page {}, Size {}",
                request.getPage(),
                request.getSize());

        Pageable pageable = PaginationUtil.getPageable(request);

        Page<District> districtPage =
                districtRepository.findByActiveTrue(pageable);

        List<DistrictResponse> response =
                districtPage.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return PageResponseUtil.of(districtPage, response);
    }

    @Override
    public DistrictResponse updateDistrict(Long id,
                                           DistrictRequest request) {

        log.info("Updating district {}", id);

        District district = districtRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "District not found with id : " + id));

        // Duplicate District Code
        if (districtRepository.existsByDistrictCodeAndIdNot(
                request.getDistrictCode(), id)) {

            throw new DuplicateResourceException(
                    "District code already exists : "
                            + request.getDistrictCode());
        }

        // Duplicate District Name in State
        if (districtRepository.existsByDistrictNameAndStateIdAndIdNot(
                request.getDistrictName(),
                request.getStateId(),
                id)) {

            throw new DuplicateResourceException(
                    "District already exists in this state.");
        }

        // Update State if changed
        if (!district.getState().getId().equals(request.getStateId())) {

            State state = stateRepository.findById(request.getStateId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "State not found"));
            district.setState(state);
        }

        district.setDistrictCode(request.getDistrictCode());
        district.setDistrictName(request.getDistrictName());

        District updatedDistrict = districtRepository.save(district);

        log.info("District updated successfully {}", updatedDistrict.getId());

        return mapToResponse(updatedDistrict);
    }

    @Override
    public void deleteDistrict(Long id) {

        log.info("Deleting district {}", id);

        District district = districtRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "District not found with id : " + id));

        district.setActive(false);

        districtRepository.save(district);

        log.info("District soft deleted successfully {}", id);
    }

    @Override
    public List<DistrictResponse> getDistrictsByState(Long stateId) {

        return districtRepository.findByStateIdAndActiveTrue(stateId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private DistrictResponse mapToResponse(District district) {

        return DistrictResponse.builder()
                .id(district.getId())
                .districtCode(district.getDistrictCode())
                .districtName(district.getDistrictName())
                .stateId(district.getState().getId())
                .stateName(district.getState().getStateName())
                .active(district.getActive())
                .build();
    }
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
        if (districtRepository.existsByDistrictNameAndStateId(
                request.getDistrictName(),
                request.getStateId())) {

            throw new DuplicateResourceException(
                    "District already exists in this state.");
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
    @Override
    public PageResponse<DistrictResponse> searchDistrict(
            String keyword,
            PaginationRequest request) {

        log.info("Searching districts with keyword {}", keyword);

        Pageable pageable =
                PaginationUtil.getPageable(request);

        Page<District> districtPage;

        if (keyword == null || keyword.isBlank()) {

            districtPage =
                    districtRepository.findByActiveTrue(pageable);

        } else {

            districtPage =
                    districtRepository
                            .findByActiveTrueAndDistrictNameContainingIgnoreCaseOrActiveTrueAndDistrictCodeContainingIgnoreCase(
                                    keyword,
                                    keyword,
                                    pageable);
        }

        List<DistrictResponse> response =
                districtPage.getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return PageResponseUtil.of(
                districtPage,
                response);
    }
}