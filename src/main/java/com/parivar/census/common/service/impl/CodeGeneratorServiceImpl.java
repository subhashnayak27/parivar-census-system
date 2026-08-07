package com.parivar.census.common.service.impl;

import com.parivar.census.common.service.CodeGeneratorService;
import com.parivar.census.district.repository.DistrictRepository;
import com.parivar.census.family.repository.FamilyRepository;
import com.parivar.census.member.repository.MemberRepository;
import com.parivar.census.state.repository.StateRepository;
import com.parivar.census.village.repository.VillageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeGeneratorServiceImpl implements CodeGeneratorService {

    private final StateRepository stateRepository;
    private final DistrictRepository districtRepository;
    private final VillageRepository villageRepository;
    private final FamilyRepository familyRepository;
    private final MemberRepository memberRepository;

    @Override
    public String generateStateCode() {
        return "ST" + String.format("%02d", stateRepository.count() + 1);
    }

    @Override
    public String generateDistrictCode() {
        return "DT" + String.format("%02d", districtRepository.count() + 1);
    }

    @Override
    public String generateVillageCode() {
        return "VL" + String.format("%03d", villageRepository.count() + 1);
    }

    @Override
    public String generateFamilyCode() {
        return "FM" + String.format("%03d", familyRepository.count() + 1);
    }

    @Override
    public String generateMemberCode() {
        return "MB" + String.format("%05d", memberRepository.count() + 1);
    }

}