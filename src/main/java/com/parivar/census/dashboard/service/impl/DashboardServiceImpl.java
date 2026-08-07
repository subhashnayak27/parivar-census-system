package com.parivar.census.dashboard.service.impl;

import com.parivar.census.dashboard.dto.response.DashboardResponse;
import com.parivar.census.dashboard.service.DashboardService;
import com.parivar.census.district.repository.DistrictRepository;
import com.parivar.census.family.repository.FamilyRepository;
import com.parivar.census.member.enums.Gender;
import com.parivar.census.member.enums.MaritalStatus;
import com.parivar.census.member.repository.MemberRepository;
import com.parivar.census.state.repository.StateRepository;
import com.parivar.census.village.repository.VillageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl  implements DashboardService {
    private final StateRepository stateRepository;
    private final DistrictRepository districtRepository;
    private final VillageRepository villageRepository;
    private final FamilyRepository familyRepository;
    private final MemberRepository memberRepository;

    @Override
    public DashboardResponse getDashboard() {

        return DashboardResponse.builder()

                .totalStates(stateRepository.count())

                .totalDistricts(districtRepository.count())

                .totalVillages(villageRepository.count())

                .totalFamilies(familyRepository.count())

                .totalMembers(memberRepository.count())

                .activeMembers(memberRepository.countByActiveTrue())

                .maleMembers(memberRepository.countByGender(Gender.MALE))

                .femaleMembers(memberRepository.countByGender(Gender.FEMALE))

                .otherMembers(memberRepository.countByGender(Gender.OTHER))

                .marriedMembers(memberRepository.countByMaritalStatus(MaritalStatus.MARRIED))

                .singleMembers(memberRepository.countByMaritalStatus(MaritalStatus.SINGLE))

                .build();
    }
}
