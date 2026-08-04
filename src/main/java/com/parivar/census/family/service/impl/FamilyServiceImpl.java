package com.parivar.census.family.service.impl;

import com.parivar.census.district.entity.district.District;
import com.parivar.census.exception.ResourceNotFoundException;
import com.parivar.census.family.dto.request.FamilyRequest;
import com.parivar.census.family.dto.response.FamilyResponse;
import com.parivar.census.family.entity.Family;
import com.parivar.census.family.repository.FamilyRepository;
import com.parivar.census.family.service.FamilyService;
import com.parivar.census.state.entity.State.State;
import com.parivar.census.village.entity.village.Village;
import com.parivar.census.village.repository.VillageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FamilyServiceImpl implements FamilyService {

    private final FamilyRepository familyRepository;
    private final VillageRepository villageRepository;

    @Override
    public FamilyResponse createFamily(FamilyRequest request) {

        log.info("Creating Family. VillageId: {}", request.getVillageId());

        Village village = villageRepository.findById(request.getVillageId())
                .orElseThrow(() -> {
                    log.warn("Village not found with id {}", request.getVillageId());

                    return new ResourceNotFoundException(
                            "Village not found with id : " + request.getVillageId());
                });

        Family family = mapToEntity(request, village);

        // Save first to generate ID
        Family savedFamily = familyRepository.save(family);

        // Generate Family Code
        savedFamily.setFamilyCode(
                "FAM" + String.format("%05d", savedFamily.getId())
        );

        savedFamily = familyRepository.save(savedFamily);

        log.info("Family created successfully. Id: {}, Code: {}",
                savedFamily.getId(),
                savedFamily.getFamilyCode());

        return mapToResponse(savedFamily);
    }

    @Override
    public FamilyResponse getFamilyById(Long id) {

        log.info("Fetching family with id {}", id);

        Family family = familyRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Family not found with id {}", id);

                    return new ResourceNotFoundException(
                            "Family not found with id : " + id);
                });

        if (!family.getActive()) {

            throw new ResourceNotFoundException(
                    "Family not found with id : " + id);
        }

        return mapToResponse(family);
    }

    @Override
    public List<FamilyResponse> getAllFamilies() {

        log.info("Fetching all active families");

        return familyRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public FamilyResponse updateFamily(Long id,
                                       FamilyRequest request) {

        log.info("Updating family {}", id);

        Family family = familyRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Family not found {}", id);

                    return new ResourceNotFoundException(
                            "Family not found with id : " + id);
                });

        if (!family.getVillage().getId().equals(request.getVillageId())) {

            Village village = villageRepository.findById(request.getVillageId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Village not found with id : "
                                    + request.getVillageId()));

            family.setVillage(village);
        }

        family.setFamilyHeadName(request.getFamilyHeadName());
        family.setAddress(request.getAddress());
        family.setMobileNo(request.getMobileNo());
        family.setRationCardNo(request.getRationCardNo());

        Family updatedFamily = familyRepository.save(family);

        log.info("Family updated successfully. Id: {}",
                updatedFamily.getId());

        return mapToResponse(updatedFamily);
    }

    @Override
    public void deleteFamily(Long id) {

        log.info("Deleting family {}", id);

        Family family = familyRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Family not found {}", id);

                    return new ResourceNotFoundException(
                            "Family not found with id : " + id);
                });

        family.setActive(false);

        familyRepository.save(family);

        log.info("Family soft deleted successfully. Id: {}", id);
    }

    @Override
    public List<FamilyResponse> getFamiliesByVillage(Long villageId) {

        log.info("Fetching families for village {}", villageId);

        return familyRepository.findByVillageId(villageId)
                .stream()
                .filter(Family::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    private Family mapToEntity(FamilyRequest request,
                               Village village) {

        return Family.builder()
                .familyHeadName(request.getFamilyHeadName())
                .address(request.getAddress())
                .mobileNo(request.getMobileNo())
                .rationCardNo(request.getRationCardNo())
                .village(village)
                .build();
    }

    private FamilyResponse mapToResponse(Family family) {

        Village village = family.getVillage();

        District district = village.getDistrict();

        State state = district.getState();

        return FamilyResponse.builder()
                .id(family.getId())
                .familyCode(family.getFamilyCode())
                .familyHeadName(family.getFamilyHeadName())
                .address(family.getAddress())
                .mobileNo(family.getMobileNo())
                .rationCardNo(family.getRationCardNo())
                .villageId(village.getId())
                .villageName(village.getVillageName())
                .districtId(district.getId())
                .districtName(district.getDistrictName())
                .stateId(state.getId())
                .stateName(state.getStateName())
                .active(family.getActive())
                .build();
    }
}