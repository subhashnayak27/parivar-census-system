package com.parivar.census.village.service;

import com.parivar.census.village.dto.request.VillageRequest;
import com.parivar.census.village.dto.response.VillageResponse;

public interface VillageService {

    VillageResponse createVillage(VillageRequest request);

}