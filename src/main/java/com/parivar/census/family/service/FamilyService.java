package com.parivar.census.family.service;

import com.parivar.census.family.dto.request.FamilyRequest;
import com.parivar.census.family.dto.response.FamilyResponse;

import java.util.List;

public interface FamilyService {

    FamilyResponse createFamily(FamilyRequest request);
    FamilyResponse getFamilyById(Long id);
    List<FamilyResponse> getAllFamilies();
    List<FamilyResponse> getFamiliesByVillage(Long villageId);
    FamilyResponse updateFamily(Long id, FamilyRequest request);

    void deleteFamily(Long id);

}