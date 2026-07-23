package com.parivar.census.member.service.impl;

import com.parivar.census.exception.DuplicateResourceException;
import com.parivar.census.exception.ResourceNotFoundException;
import com.parivar.census.family.entity.Family;
import com.parivar.census.family.repository.FamilyRepository;
import com.parivar.census.member.dto.request.MemberRequest;
import com.parivar.census.member.dto.response.MemberResponse;
import com.parivar.census.member.entity.Member;
import com.parivar.census.member.repository.MemberRepository;
import com.parivar.census.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
                .familyId(member.getFamily().getId())
                .familyCode(member.getFamily().getFamilyCode())
                .familyHeadName(member.getFamily().getFamilyHeadName())
                .active(member.getActive())
                .build();
    }

}