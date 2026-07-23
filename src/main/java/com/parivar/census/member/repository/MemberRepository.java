package com.parivar.census.member.repository;

import com.parivar.census.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // Duplicate validation
    boolean existsByMemberCode(String memberCode);

    boolean existsByAadhaarNo(String aadhaarNo);

    // Family wise members
    List<Member> findByFamilyId(Long familyId);

    // Living members
    List<Member> findByAliveTrue();

    // Active members
    List<Member> findByActiveTrue();

}