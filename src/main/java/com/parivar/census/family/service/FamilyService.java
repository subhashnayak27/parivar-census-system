package com.parivar.census.family.service;

import com.parivar.census.family.dto.request.FamilyRequest;
import com.parivar.census.family.dto.response.FamilyResponse;

public interface FamilyService {

    FamilyResponse createFamily(FamilyRequest request);

}