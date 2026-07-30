package com.parivar.census.family.repository;

import com.parivar.census.family.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyRepository extends JpaRepository<Family, Long> {

    boolean existsByFamilyCode(String familyCode);
    boolean existsByFamilyCodeAndIdNot(String familyCode, Long id);
    List<Family> findByActiveTrue();
    List<Family> findByVillageId(Long villageId);

}