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
                .active(village.getActive())
                .build();
    }
}