package com.parivar.census.member.service;

import java.util.List;

public interface MemberExportService {

    default byte[] exportMembers() {
        return exportMembers(null, null);
    }

    default byte[] exportMembers(List<Long> memberIds) {
        return exportMembers(null, memberIds);
    }

    byte[] exportMembers(Long familyId, List<Long> memberIds);

}