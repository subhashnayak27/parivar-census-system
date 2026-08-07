package com.parivar.census.district.repository;
import com.parivar.census.district.entity.district.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {

    boolean existsByDistrictCode(String districtCode);
    Page<District> findByActiveTrue(Pageable pageable);
    List<District> findByStateIdAndActiveTrue(Long stateId);
    boolean existsByDistrictCodeAndIdNot(String districtCode, Long id);
    boolean existsByDistrictNameAndStateIdAndIdNot( String districtName, Long stateId, Long id);

}