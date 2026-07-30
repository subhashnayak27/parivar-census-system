package com.parivar.census.district.repository;

import com.parivar.census.district.entity.district.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {

    boolean existsByDistrictCode(String districtCode);
    List<District> findByActiveTrue();
    List<District> findByStateIdAndActiveTrue(Long stateId);
    boolean existsByDistrictCodeAndIdNot(String districtCode, Long id);
    boolean existsByDistrictNameAndStateIdAndIdNot(
            String districtName,
            Long stateId,
            Long id);
}