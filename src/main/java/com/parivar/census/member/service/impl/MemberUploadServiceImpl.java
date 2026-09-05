package com.parivar.census.member.service.impl;

import com.parivar.census.family.entity.Family;
import com.parivar.census.family.repository.FamilyRepository;
import com.parivar.census.member.dto.request.MemberUploadRequest;
import com.parivar.census.member.dto.response.MemberUploadResponse;
import com.parivar.census.member.entity.Member;
import com.parivar.census.member.repository.MemberRepository;
import com.parivar.census.member.service.MemberUploadService;
import com.parivar.census.member.util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.parivar.census.exception.DuplicateResourceException;
import com.parivar.census.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberUploadServiceImpl implements MemberUploadService {

    private final MemberRepository memberRepository;
    private final FamilyRepository familyRepository;

    @Override
    public MemberUploadResponse uploadMembers(MultipartFile file) {

        List<MemberUploadRequest> requests = ExcelUtil.readMembers(file);
        int total = 0;
        int success = 0;
        int failed = 0;

        List<String> errors = new ArrayList<>();
        for (MemberUploadRequest request : requests) {

            total++;

            try {

                // Find Family
                Family family = familyRepository.findByFamilyCode(request.getFamilyCode())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Family not found : " + request.getFamilyCode()));

// Aadhaar Validation (Optional)
                if (request.getAadhaarNo() != null
                        && !request.getAadhaarNo().isBlank()
                        && memberRepository.existsByAadhaarNo(request.getAadhaarNo())) {

                    throw new DuplicateResourceException(
                            "Aadhaar already exists : " + request.getAadhaarNo());
                }

// Create Member Entity
                Member member = Member.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .gender(request.getGender())
                        .dateOfBirth(request.getDateOfBirth())
                        .birthTime(request.getBirthTime())
                        .alive(request.getAlive())
                        .dateOfDeath(request.getDateOfDeath())
                        .relationship(request.getRelationship())
                        .maritalStatus(request.getMaritalStatus())
                        .mobileNo(request.getMobileNo())
                        .aadhaarNo(
                                request.getAadhaarNo() == null || request.getAadhaarNo().isBlank()
                                        ? null
                                        : request.getAadhaarNo())
                        .occupation(request.getOccupation())
                        .education(request.getEducation())
                        .gotra(request.getGotra())
                        .pata(request.getPata())
                        .kuldevi(request.getKuldevi())
                        .family(family)
                        .build();

// Save to generate ID
                Member saved = memberRepository.save(member);

// Generate Member Code
                saved.setMemberCode(
                        "MEM" + String.format("%05d", saved.getId())
                );

// Save again
                memberRepository.save(saved);

                success++;

            } catch (Exception ex) {

                failed++;
                errors.add("Row " + total + " : " + ex.getMessage());
            }
        }

        return MemberUploadResponse.builder()
                .totalRecords(total)
                .successRecords(success)
                .failedRecords(failed)
                .errors(errors)
                .build();
    }
}