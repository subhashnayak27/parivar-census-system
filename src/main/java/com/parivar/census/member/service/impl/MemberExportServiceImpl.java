package com.parivar.census.member.service.impl;

import com.parivar.census.member.entity.Member;
import com.parivar.census.member.repository.MemberRepository;
import com.parivar.census.member.service.MemberExportService;
import com.parivar.census.member.util.MemberExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberExportServiceImpl implements MemberExportService {

    private final MemberRepository memberRepository;

    @Override
    public byte[] exportMembers() {

        List<Member> members =
                memberRepository.findByActiveTrue();

        return MemberExcelExportUtil.exportMembers(members);
    }
}