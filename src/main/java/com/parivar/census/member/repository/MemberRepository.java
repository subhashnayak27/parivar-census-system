package com.parivar.census.member.repository;

import com.parivar.census.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMemberCode(String memberCode);

    boolean existsByAadhaarNo(String aadhaarNo);

    boolean existsByMemberCodeAndIdNot(String memberCode, Long id);

    boolean existsByAadhaarNoAndIdNot(String aadhaarNo, Long id);

    List<Member> findByFamilyId(Long familyId);

    List<Member> findByAliveTrue();

    List<Member> findByActiveTrue();

}