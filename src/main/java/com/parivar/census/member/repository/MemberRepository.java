package com.parivar.census.member.repository;

import com.parivar.census.member.entity.Member;
import com.parivar.census.member.enums.Gender;
import com.parivar.census.member.enums.MaritalStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMemberCode(String memberCode);
    boolean existsByAadhaarNo(String aadhaarNo);
    boolean existsByMemberCodeAndIdNot(
            String memberCode,
            Long id
    );
    boolean existsByAadhaarNoAndIdNot(String aadhaarNo, Long id);
    long countByActiveTrue();
    long countByGender(Gender gender);
    long countByMaritalStatus(MaritalStatus maritalStatus);

    List<Member> findByActiveTrue();
    List<Member> findByFamilyId(Long familyId);
    List<Member> findByAliveTrue();

    Page<Member> findByActiveTrue(Pageable pageable);

    @Query("""
        SELECT m
        FROM Member m
        LEFT JOIN m.family f
        WHERE m.active = true
          AND (
                LOWER(m.memberCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(m.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(m.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR m.mobileNo LIKE CONCAT('%', :keyword, '%')
             OR m.aadhaarNo LIKE CONCAT('%', :keyword, '%')
             OR LOWER(f.familyCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(f.familyHeadName) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        """)
    Page<Member> searchMembers(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}