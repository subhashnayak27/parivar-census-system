package com.parivar.census.member.service.impl;

import com.parivar.census.member.entity.Member;
import com.parivar.census.member.repository.MemberRepository;
import com.parivar.census.member.service.MemberExportService;
import com.parivar.census.member.util.MemberExcelExportUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberExportServiceImpl implements MemberExportService {

    private final MemberRepository memberRepository;

    @Override
    public byte[] exportMembers(Long familyId, List<Long> memberIds) {

        List<Member> members;

        if (familyId != null) {
            members = memberRepository.findByFamilyId(familyId);
        } else if (memberIds == null || memberIds.isEmpty()) {
            members = memberRepository.findByActiveTrue();
        } else {
            members = memberRepository.findAllById(memberIds);
            members.sort(Comparator.comparingInt(member ->
                    memberIds.indexOf(member.getId())));
        }

        return MemberExcelExportUtil.exportMembers(members);
    }
}