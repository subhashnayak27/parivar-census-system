package com.parivar.census.member.service.impl;

import com.parivar.census.district.entity.district.District;
import com.parivar.census.exception.DuplicateResourceException;
import com.parivar.census.exception.ResourceNotFoundException;
import com.parivar.census.family.entity.Family;
import com.parivar.census.family.repository.FamilyRepository;
import com.parivar.census.member.dto.request.MemberRequest;
import com.parivar.census.member.dto.response.MemberResponse;
import com.parivar.census.member.entity.Member;
import com.parivar.census.member.repository.MemberRepository;
import com.parivar.census.member.service.MemberService;
import com.parivar.census.state.entity.State.State;
import com.parivar.census.village.entity.village.Village;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final FamilyRepository familyRepository;

    @Override
    public MemberResponse createMember(MemberRequest request) {

        log.info("Creating member");

        // Duplicate Aadhaar
        if (request.getAadhaarNo() != null
                && !request.getAadhaarNo().isBlank()
                && memberRepository.existsByAadhaarNo(request.getAadhaarNo())) {

            throw new DuplicateResourceException(
                    "Aadhaar already exists : " + request.getAadhaarNo());
        }

        Family family = familyRepository.findById(request.getFamilyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Family not found with id : " + request.getFamilyId()));

        if (Boolean.FALSE.equals(request.getAlive())
                && request.getDateOfDeath() == null) {

            throw new IllegalArgumentException(
                    "Date of Death is required when member is deceased.");
        }

        Member member = mapToEntity(request, family);

        // Temporary value (member_code is NOT NULL)
        member.setMemberCode("TEMP");

        Member savedMember = memberRepository.save(member);

        // Generate Business Code
        savedMember.setMemberCode(
                "MEM" + String.format("%05d", savedMember.getId())
        );

        savedMember = memberRepository.save(savedMember);

        log.info("Member created successfully with id {}",
                savedMember.getId());

        return mapToResponse(savedMember);
    }

    @Override
    public MemberResponse updateMember(Long id, MemberRequest request) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found with id : " + id));

        if (request.getAadhaarNo() != null
                && !request.getAadhaarNo().isBlank()
                && memberRepository.existsByAadhaarNoAndIdNot(
                request.getAadhaarNo(), id)) {

            throw new DuplicateResourceException(
                    "Aadhaar already exists : "
                            + request.getAadhaarNo());
        }

        if (Boolean.FALSE.equals(request.getAlive())
                && request.getDateOfDeath() == null) {

            throw new IllegalArgumentException(
                    "Date of Death is required when member is deceased.");
        }

        if (!member.getFamily().getId().equals(request.getFamilyId())) {

            Family family = familyRepository.findById(request.getFamilyId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Family not found with id : "
                                            + request.getFamilyId()));

            member.setFamily(family);
        }

        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setGender(request.getGender());
        member.setDateOfBirth(request.getDateOfBirth());
        member.setAlive(request.getAlive());
        member.setDateOfDeath(request.getDateOfDeath());
        member.setRelationship(request.getRelationship());
        member.setMaritalStatus(request.getMaritalStatus());
        member.setMobileNo(request.getMobileNo());
        member.setAadhaarNo( request.getAadhaarNo() == null || request.getAadhaarNo().isBlank() ? null  : request.getAadhaarNo().trim());
        member.setOccupation(request.getOccupation());
        member.setEducation(request.getEducation());
        member.setGotra(request.getGotra());
        member.setPata(request.getPata());
        member.setKuldevi(request.getKuldevi());

        Member updatedMember = memberRepository.save(member);

        return mapToResponse(updatedMember);
    }

    @Override
    public MemberResponse getMemberById(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found with id : " + id));

        if (!member.getActive()) {
            throw new ResourceNotFoundException(
                    "Member not found with id : " + id);
        }

        return mapToResponse(member);
    }

    @Override
    public List<MemberResponse> getAllMembers() {

        return memberRepository.findAll()
                .stream()
                .filter(Member::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteMember(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Member not found with id : " + id));

        member.setActive(false);

        memberRepository.save(member);
    }

    private Member mapToEntity(MemberRequest request,
                               Family family) {

        return Member.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .alive(request.getAlive())
                .dateOfDeath(request.getDateOfDeath())
                .relationship(request.getRelationship())
                .maritalStatus(request.getMaritalStatus())
                .mobileNo(request.getMobileNo())
                .aadhaarNo(request.getAadhaarNo() == null || request.getAadhaarNo().isBlank() ? null : request.getAadhaarNo().trim())
                .occupation(request.getOccupation())
                .education(request.getEducation())
                .gotra(request.getGotra())
                .pata(request.getPata())
                .kuldevi(request.getKuldevi())
                .family(family)
                .build();
    }

    private MemberResponse mapToResponse(Member member) {

        Family family = member.getFamily();
        Village village = family.getVillage();
        District district = village.getDistrict();
        State state = district.getState();

        return MemberResponse.builder()
                .id(member.getId())
                .memberCode(member.getMemberCode())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .gender(member.getGender())
                .dateOfBirth(member.getDateOfBirth())
                .alive(member.getAlive())
                .dateOfDeath(member.getDateOfDeath())
                .relationship(member.getRelationship())
                .maritalStatus(member.getMaritalStatus())
                .mobileNo(member.getMobileNo())
                .aadhaarNo(member.getAadhaarNo())
                .occupation(member.getOccupation())
                .education(member.getEducation())
                .gotra(member.getGotra())
                .pata(member.getPata())
                .kuldevi(member.getKuldevi())
                .familyId(family.getId())
                .familyCode(family.getFamilyCode())
                .familyHeadName(family.getFamilyHeadName())
                .villageId(village.getId())
                .villageName(village.getVillageName())
                .districtId(district.getId())
                .districtName(district.getDistrictName())
                .stateId(state.getId())
                .stateName(state.getStateName())
                .active(member.getActive())
                .build();
    }
}