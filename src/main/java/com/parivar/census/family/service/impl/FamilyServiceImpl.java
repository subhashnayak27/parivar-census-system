package com.parivar.census.family.service.impl;

import com.parivar.census.exception.DuplicateResourceException;
import com.parivar.census.exception.ResourceNotFoundException;
import com.parivar.census.family.dto.request.FamilyRequest;
import com.parivar.census.family.dto.response.FamilyResponse;
import com.parivar.census.family.entity.Family;
import com.parivar.census.family.repository.FamilyRepository;
import com.parivar.census.family.service.FamilyService;
import com.parivar.census.village.entity.village.Village;
import com.parivar.census.village.repository.VillageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FamilyServiceImpl implements FamilyService {

    private final FamilyRepository familyRepository;
    private final VillageRepository villageRepository;

    @Override
    public FamilyResponse createFamily(FamilyRequest request) {

        log.info("Creating Family. Code: {}, VillageId: {}",
                request.getFamilyCode(),
                request.getVillageId());

        // Check duplicate Family Code
        if (familyRepository.existsByFamilyCode(request.getFamilyCode())) {
            log.warn("Family with code {} already exists",
                    request.getFamilyCode());

            throw new DuplicateResourceException(
                    "Family code already exists : " + request.getFamilyCode());
        }

        // Find Village
        Village village = villageRepository.findById(request.getVillageId())
                .orElseThrow(() -> {
                    log.warn("Village not found with id {}",
                            request.getVillageId());

                    return new ResourceNotFoundException(
                            "Village not found with id : " + request.getVillageId());
                });

        // Convert Request DTO to Entity
        Family family = mapToEntity(request, village);

        // Save Entity
        Family savedFamily = familyRepository.save(family);

        log.info("Family created successfully. Id: {}, Code: {}",
                savedFamily.getId(),
                savedFamily.getFamilyCode());

        // Convert Entity to Response DTO
        return mapToResponse(savedFamily);
    }

    private Family mapToEntity(FamilyRequest request, Village village) {

        return Family.builder()
                .familyCode(request.getFamilyCode())
                .familyHeadName(request.getFamilyHeadName())
                .address(request.getAddress())
                .mobileNo(request.getMobileNo())
                .rationCardNo(request.getRationCardNo())
                .village(village)
                .build();
    }

    private FamilyResponse mapToResponse(Family family) {

        return FamilyResponse.builder()
                .id(family.getId())
                .familyCode(family.getFamilyCode())
                .familyHeadName(family.getFamilyHeadName())
                .address(family.getAddress())
                .mobileNo(family.getMobileNo())
                .rationCardNo(family.getRationCardNo())
                .villageId(family.getVillage().getId())
                .villageName(family.getVillage().getVillageName())
                .active(family.getActive())
                .build();
    }
}