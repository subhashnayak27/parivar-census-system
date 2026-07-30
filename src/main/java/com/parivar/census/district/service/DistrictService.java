package com.parivar.census.district.service;

import com.parivar.census.district.dto.request.DistrictRequest;
import com.parivar.census.district.dto.response.DistrictResponse;

import java.util.List;

public interface DistrictService {

    DistrictResponse createDistrict(DistrictRequest request);
    DistrictResponse getDistrictById(Long id);
    List<DistrictResponse> getAllDistricts();
    DistrictResponse updateDistrict(Long id, DistrictRequest request);
    void deleteDistrict(Long id);
    List<DistrictResponse> getDistrictsByState(Long stateId);
}