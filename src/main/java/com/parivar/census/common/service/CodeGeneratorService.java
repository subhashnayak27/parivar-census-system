package com.parivar.census.common.service;

public interface CodeGeneratorService {

    String generateStateCode();

    String generateDistrictCode();

    String generateVillageCode();

    String generateFamilyCode();

    String generateMemberCode();

}