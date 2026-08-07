package com.parivar.census.family.service;

import com.parivar.census.common.dto.PageResponse;
import com.parivar.census.common.dto.PaginationRequest;
import com.parivar.census.family.dto.request.FamilyRequest;
import com.parivar.census.family.dto.response.FamilyResponse;

import java.util.List;

public interface FamilyService {

    FamilyResponse createFamily(FamilyRequest request);
    FamilyResponse getFamilyById(Long id);
    PageResponse<FamilyResponse> getAllFamilies(PaginationRequest request);
    List<FamilyResponse> getFamiliesByVillage(Long villageId);
    FamilyResponse updateFamily(Long id, FamilyRequest request);

    void deleteFamily(Long id);

}