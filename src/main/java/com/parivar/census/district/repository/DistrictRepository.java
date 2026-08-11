package com.parivar.census.district.repository;

import com.parivar.census.district.entity.district.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {

    boolean existsByDistrictCode(String districtCode);

    Page<District> findByActiveTrue(Pageable pageable);

    List<District> findByStateIdAndActiveTrue(Long stateId);

    boolean existsByDistrictNameAndStateId(
            String districtName,
            Long stateId);
    boolean existsByDistrictCodeAndIdNot(
            String districtCode,
            Long id);

    boolean existsByDistrictNameAndStateIdAndIdNot(
            String districtName,
            Long stateId,
            Long id);

    Page<District>
    findByActiveTrueAndDistrictNameContainingIgnoreCaseOrActiveTrueAndDistrictCodeContainingIgnoreCase(
            String districtName,
            String districtCode,
            Pageable pageable);
}