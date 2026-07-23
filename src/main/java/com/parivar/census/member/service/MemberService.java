package com.parivar.census.member.service;

import com.parivar.census.member.dto.request.MemberRequest;
import com.parivar.census.member.dto.response.MemberResponse;

public interface MemberService {

    MemberResponse createMember(MemberRequest request);

}