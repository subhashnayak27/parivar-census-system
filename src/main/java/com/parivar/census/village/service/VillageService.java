package com.parivar.census.village.service;

import com.parivar.census.common.dto.PageResponse;
import com.parivar.census.common.dto.PaginationRequest;
import com.parivar.census.village.dto.request.VillageRequest;
import com.parivar.census.village.dto.response.VillageResponse;
import java.util.List;
public interface VillageService {

    VillageResponse createVillage(VillageRequest request);
    VillageResponse getVillageById(Long id);
    PageResponse<VillageResponse> getAllVillages( PaginationRequest request);
    VillageResponse updateVillage(Long id, VillageRequest request);
    void deleteVillage(Long id);
    List<VillageResponse> getVillagesByDistrict(Long districtId);
}