package com.parivar.census.member.repository;

import com.parivar.census.family.entity.Family;
import com.parivar.census.member.entity.Member;
import com.parivar.census.member.enums.Gender;
import com.parivar.census.member.enums.MaritalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMemberCode(String memberCode);
    boolean existsByAadhaarNo(String aadhaarNo);
    boolean existsByMemberCodeAndIdNot(String memberCode, Long id);
    boolean existsByAadhaarNoAndIdNot(String aadhaarNo, Long id);
    long countByActiveTrue();
    long countByGender(Gender gender);
    long countByMaritalStatus(MaritalStatus maritalStatus);
    List<Member> findByActiveTrue();
    List<Member> findByFamilyId(Long familyId);
    List<Member> findByAliveTrue();
    Page<Member> findByActiveTrue(Pageable pageable);


}