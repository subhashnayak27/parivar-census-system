package com.parivar.census.member.service;

import com.parivar.census.common.dto.PageResponse;
import com.parivar.census.common.dto.PaginationRequest;
import com.parivar.census.member.dto.request.MemberRequest;
import com.parivar.census.member.dto.response.MemberResponse;

import java.util.List;

public interface MemberService {

    MemberResponse createMember(MemberRequest request);
    MemberResponse getMemberById(Long id);
    List<MemberResponse> getMembersByFamilyId(Long familyId);
    PageResponse<MemberResponse> getAllMembers(PaginationRequest request);
    MemberResponse updateMember(Long id, MemberRequest request);
    void deleteMember(Long id);
    PageResponse<MemberResponse> searchMembers(
            String keyword,
            PaginationRequest request);
}