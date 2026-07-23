package com.parivar.census.district.service;

import com.parivar.census.district.dto.request.DistrictRequest;
import com.parivar.census.district.dto.response.DistrictResponse;

public interface DistrictService {

    DistrictResponse createDistrict(DistrictRequest request);

}