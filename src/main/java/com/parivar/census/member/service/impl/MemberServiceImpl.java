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

        log.info("Creating member with code : {}", request.getMemberCode());

        // Duplicate Member Code
        if (memberRepository.existsByMemberCode(request.getMemberCode())) {

            log.warn("Member code {} already exists",
                    request.getMemberCode());

            throw new DuplicateResourceException(
                    "Member code already exists : "
                            + request.getMemberCode());
        }

        // Duplicate Aadhaar
        if (request.getAadhaarNo() != null &&
                memberRepository.existsByAadhaarNo(request.getAadhaarNo())) {

            log.warn("Aadhaar {} already exists",
                    request.getAadhaarNo());

            throw new DuplicateResourceException(
                    "Aadhaar already exists : "
                            + request.getAadhaarNo());
        }

        // Find Family
        Family family = familyRepository.findById(request.getFamilyId())
                .orElseThrow(() -> {

                    log.warn("Family not found with id {}",
                            request.getFamilyId());

                    return new ResourceNotFoundException(
                            "Family not found with id : "
                                    + request.getFamilyId());
                });

        // Business Validation
        if (Boolean.FALSE.equals(request.getAlive())
                && request.getDateOfDeath() == null) {

            throw new IllegalArgumentException(
                    "Date of Death is required when member is deceased.");
        }

        // Request -> Entity
        Member member = mapToEntity(request, family);

        // Save
        Member savedMember = memberRepository.save(member);

        log.info("Member created successfully with id {}",
                savedMember.getId());

        // Entity -> Response
        return mapToResponse(savedMember);
    }

    @Override
    public MemberResponse updateMember(Long id, MemberRequest request) {

        log.info("Updating member with id {}", id);

        // 1. Find existing member
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Member not found with id {}", id);
                    return new ResourceNotFoundException(
                            "Member not found with id : " + id);
                });

        // 2. Duplicate Member Code Validation
        if (memberRepository.existsByMemberCodeAndIdNot(
                request.getMemberCode(), id)) {

            throw new DuplicateResourceException(
                    "Member code already exists : " + request.getMemberCode());
        }

        // 3. Duplicate Aadhaar Validation
        if (request.getAadhaarNo() != null &&
                memberRepository.existsByAadhaarNoAndIdNot(
                        request.getAadhaarNo(), id)) {

            throw new DuplicateResourceException(
                    "Aadhaar already exists : " + request.getAadhaarNo());
        }

        // 4. Business Validation
        if (Boolean.FALSE.equals(request.getAlive())
                && request.getDateOfDeath() == null) {

            throw new IllegalArgumentException(
                    "Date of Death is required when member is deceased.");
        }

        // 5. Update Family if changed
        if (!member.getFamily().getId().equals(request.getFamilyId())) {

            Family family = familyRepository.findById(request.getFamilyId())
                    .orElseThrow(() -> {
                        log.warn("Family not found with id {}", request.getFamilyId());
                        return new ResourceNotFoundException(
                                "Family not found with id : " + request.getFamilyId());
                    });

            member.setFamily(family);
        }

        // 6. Update member fields
        member.setMemberCode(request.getMemberCode());
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        // ... remaining setters

        // 7. Save
        Member updatedMember = memberRepository.save(member);

        // 8. Return
        return mapToResponse(updatedMember);
    }
    /**
     * Request DTO -> Entity
     */
    private Member mapToEntity(MemberRequest request,
                               Family family) {

        return Member.builder()
                .memberCode(request.getMemberCode())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .alive(request.getAlive())
                .dateOfDeath(request.getDateOfDeath())
                .relationship(request.getRelationship())
                .maritalStatus(request.getMaritalStatus())
                .mobileNo(request.getMobileNo())
                .aadhaarNo(request.getAadhaarNo())
                .occupation(request.getOccupation())
                .education(request.getEducation())
                .family(family)
                .build();
    }

    /**
     * Entity -> Response DTO
     */
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
    @Override
    public MemberResponse getMemberById(Long id) {

        log.info("Fetching member with id: {}", id);

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Member not found with id {}", id);
                    return new ResourceNotFoundException(
                            "Member not found with id : " + id);
                });

        return mapToResponse(member);
    }
    @Override
    public List<MemberResponse> getAllMembers() {

        log.info("Fetching all members");

        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteMember(Long id) {

        log.info("Deleting member with id {}", id);

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Member not found with id {}", id);

                    return new ResourceNotFoundException(
                            "Member not found with id : " + id);
                });

        member.setActive(false);

        memberRepository.save(member);

        log.info("Member soft deleted successfully. Id: {}", id);
    }
}