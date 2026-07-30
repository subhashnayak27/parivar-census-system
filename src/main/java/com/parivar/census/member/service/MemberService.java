package com.parivar.census.member.service;

import com.parivar.census.member.dto.request.MemberRequest;
import com.parivar.census.member.dto.response.MemberResponse;
import com.parivar.census.member.entity.Member;

import java.util.List;

public interface MemberService {

    MemberResponse createMember(MemberRequest request);
    MemberResponse getMemberById(Long id);
    List<MemberResponse> getAllMembers();
    MemberResponse updateMember(Long id, MemberRequest request);
    void deleteMember(Long id);
}