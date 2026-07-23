package com.parivar.census.village.repository;

import com.parivar.census.village.entity.village.Village;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VillageRepository extends JpaRepository<Village, Long> {

    boolean existsByVillageCode(String villageCode);

    Optional<Village> findByVillageCode(String villageCode);

    List<Village> findByDistrictId(Long districtId);

}