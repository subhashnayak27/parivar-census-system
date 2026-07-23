package com.parivar.census.district.repository;

import com.parivar.census.district.entity.district.District;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, Long> {

    boolean existsByDistrictCode(String districtCode);

}