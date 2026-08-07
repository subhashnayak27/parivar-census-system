package com.parivar.census.member.util;

import com.parivar.census.member.dto.request.MemberUploadRequest;
import com.parivar.census.member.enums.Gender;
import com.parivar.census.member.enums.MaritalStatus;
import com.parivar.census.member.enums.RelationshipType;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private ExcelUtil() {
    }

    public static List<MemberUploadRequest> readMembers(MultipartFile file) {

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

            List<MemberUploadRequest> members = new ArrayList<>();

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                MemberUploadRequest request = new MemberUploadRequest();

                request.setFamilyCode(getCellValue(row.getCell(0)));
                request.setFirstName(getCellValue(row.getCell(1)));
                request.setLastName(getCellValue(row.getCell(2)));

                request.setGender(
                        Gender.valueOf(getCellValue(row.getCell(3)).toUpperCase())
                );

                request.setDateOfBirth(
                        LocalDate.parse(getCellValue(row.getCell(4)))
                );

                request.setAlive(
                        Boolean.parseBoolean(getCellValue(row.getCell(5)))
                );

                String dod = getCellValue(row.getCell(6));

                if (!dod.isBlank()) {
                    request.setDateOfDeath(LocalDate.parse(dod));
                }

                request.setRelationship(
                        RelationshipType.valueOf(
                                getCellValue(row.getCell(7)).toUpperCase())
                );

                request.setMaritalStatus(
                        MaritalStatus.valueOf(
                                getCellValue(row.getCell(8)).toUpperCase())
                );

                request.setMobileNo(getCellValue(row.getCell(9)));
                request.setAadhaarNo(getCellValue(row.getCell(10)));
                request.setOccupation(getCellValue(row.getCell(11)));
                request.setEducation(getCellValue(row.getCell(12)));
                request.setGotra(getCellValue(row.getCell(13)));
                request.setPata(getCellValue(row.getCell(14)));
                request.setKuldevi(getCellValue(row.getCell(15)));

                members.add(request);
            }

            return members;

        } catch (IOException e) {
            throw new RuntimeException("Unable to read uploaded Excel file.", e);
        }
    }
    private static final DataFormatter FORMATTER = new DataFormatter();

    private static String getCellValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        return FORMATTER.formatCellValue(cell).trim();
    }
}