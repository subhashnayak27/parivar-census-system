package com.parivar.census.village.service.impl;

import com.parivar.census.district.entity.district.District;
import com.parivar.census.district.repository.DistrictRepository;
import com.parivar.census.exception.DuplicateResourceException;
import com.parivar.census.exception.ResourceNotFoundException;
import com.parivar.census.village.entity.village.Village;
import com.parivar.census.village.repository.VillageRepository;
import com.parivar.census.village.dto.request.VillageRequest;
import com.parivar.census.village.dto.response.VillageResponse;
import com.parivar.census.village.service.VillageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VillageServiceImpl implements VillageService {

    private final VillageRepository villageRepository;
    private final DistrictRepository districtRepository;


    @Override
    public VillageResponse createVillage(VillageRequest request) {

        log.info("Creating village with code: {}", request.getVillageCode());

        // Check duplicate village code
        if (villageRepository.existsByVillageCode(request.getVillageCode())) {
            log.warn("Village with code {} already exists", request.getVillageCode());
            throw new DuplicateResourceException(
                    "Village code already exists : " + request.getVillageCode());
        }

        // Find District
        District district = districtRepository.findById(request.getDistrictId())
                .orElseThrow(() -> {
                    log.warn("District not found with id {}", request.getDistrictId());
                    return new ResourceNotFoundException(
                            "District not found with id : " + request.getDistrictId());
                });

        // Convert Request → Entity
        Village village = mapToEntity(request, district);

        // Save
        Village savedVillage = villageRepository.save(village);

        log.info("Village created successfully. Id: {}, Code: {}",
                savedVillage.getId(),
                savedVillage.getVillageCode());

        // Convert Entity → Response
        return mapToResponse(savedVillage);
    }

    @Override
    public VillageResponse getVillageById(Long id) {

        log.info("Fetching village with id {}", id);

        Village village = villageRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Village not found with id {}", id);

                    return new ResourceNotFoundException(
                            "Village not found with id : " + id);
                });

        if (!village.getActive()) {
            throw new ResourceNotFoundException(
                    "Village not found with id : " + id);
        }

        return mapToResponse(village);
    }

    @Override
    public List<VillageResponse> getAllVillages() {

        log.info("Fetching all active villages");

        List<Village> villages =
                villageRepository.findByActiveTrue();

        return villages.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public VillageResponse updateVillage(Long id,
                                         VillageRequest request) {

        log.info("Updating village with id {}", id);

        Village village = villageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Village not found with id : " + id));

        // Duplicate Village Code
        if (villageRepository.existsByVillageCodeAndIdNot(
                request.getVillageCode(), id)) {

            throw new DuplicateResourceException(
                    "Village code already exists : "
                            + request.getVillageCode());
        }

        // Duplicate Village Name within District
        if (villageRepository.existsByVillageNameAndDistrictIdAndIdNot(
                request.getVillageName(),
                request.getDistrictId(),
                id)) {

            throw new DuplicateResourceException(
                    "Village already exists in this district.");
        }

        // Update District if changed
        if (!village.getDistrict().getId().equals(request.getDistrictId())) {

            District district = districtRepository.findById(
                            request.getDistrictId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "District not found"));
            village.setDistrict(district);
        }

        village.setVillageCode(request.getVillageCode());
        village.setVillageName(request.getVillageName());
        village.setPostalCode(request.getPostalCode());

        Village updatedVillage = villageRepository.save(village);

        log.info("Village updated successfully {}", updatedVillage.getId());

        return mapToResponse(updatedVillage);
    }

    @Override
    public void deleteVillage(Long id) {

        log.info("Deleting village {}", id);

        Village village = villageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Village not found with id : " + id));

        village.setActive(false);

        villageRepository.save(village);

        log.info("Village soft deleted successfully {}", id);
    }

    @Override
    public List<VillageResponse> getVillagesByDistrict(Long districtId) {

        log.info("Fetching villages for district {}", districtId);

        List<Village> villages =
                villageRepository.findByDistrictIdAndActiveTrue(districtId);

        return villages.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private Village mapToEntity(VillageRequest request, District district) {
        return Village.builder()
                .villageCode(request.getVillageCode())
                .villageName(request.getVillageName())
                .postalCode(request.getPostalCode())
                .district(district)
                .build();
    }

    private VillageResponse mapToResponse(Village village) {
        return VillageResponse.builder()
                .id(village.getId())
                .villageCode(village.getVillageCode())
                .villageName(village.getVillageName())
                .postalCode(village.getPostalCode())

                .districtId(village.getDistrict().getId())
                .districtName(village.getDistrict().getDistrictName())

                .stateId(village.getDistrict().getState().getId())
                .stateName(village.getDistrict().getState().getStateName())

                .active(village.getActive())
                .build();
    }
}