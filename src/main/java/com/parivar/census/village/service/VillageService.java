package com.parivar.census.village.service;

import com.parivar.census.village.dto.request.VillageRequest;
import com.parivar.census.village.dto.response.VillageResponse;
import java.util.List;
public interface VillageService {

    VillageResponse createVillage(VillageRequest request);
    VillageResponse getVillageById(Long id);
    List<VillageResponse> getAllVillages();
    VillageResponse updateVillage(Long id, VillageRequest request);
    void deleteVillage(Long id);
    List<VillageResponse> getVillagesByDistrict(Long districtId);
}