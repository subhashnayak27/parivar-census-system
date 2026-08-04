package com.parivar.census.member.util;

import com.parivar.census.member.entity.Member;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class MemberExcelExportUtil {

    private MemberExcelExportUtil() {
    }

    public static byte[] exportMembers(List<Member> members) {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Members");

            // Header Style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Header Row
            Row header = sheet.createRow(0);

            String[] columns = {
                    "Member Code",
                    "First Name",
                    "Last Name",
                    "Gender",
                    "Date Of Birth",
                    "Alive",
                    "Date Of Death",
                    "Relationship",
                    "Marital Status",
                    "Mobile No",
                    "Aadhaar No",
                    "Occupation",
                    "Education",
                    "Gotra",
                    "Pata",
                    "Kuldevi",
                    "Family Code",
                    "Family Head",
                    "Village",
                    "District",
                    "State"
            };

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data Rows
            int rowNum = 1;

            for (Member member : members) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(member.getMemberCode());
                row.createCell(1).setCellValue(member.getFirstName());
                row.createCell(2).setCellValue(
                        member.getLastName() == null ? "" : member.getLastName());

                row.createCell(3).setCellValue(member.getGender().name());

                row.createCell(4).setCellValue(
                        member.getDateOfBirth() == null ? "" : member.getDateOfBirth().toString());

                row.createCell(5).setCellValue(member.getAlive());

                row.createCell(6).setCellValue(
                        member.getDateOfDeath() == null ? "" : member.getDateOfDeath().toString());

                row.createCell(7).setCellValue(member.getRelationship().name());

                row.createCell(8).setCellValue(member.getMaritalStatus().name());

                row.createCell(9).setCellValue(
                        member.getMobileNo() == null ? "" : member.getMobileNo());

                row.createCell(10).setCellValue(
                        member.getAadhaarNo() == null ? "" : member.getAadhaarNo());

                row.createCell(11).setCellValue(
                        member.getOccupation() == null ? "" : member.getOccupation());

                row.createCell(12).setCellValue(
                        member.getEducation() == null ? "" : member.getEducation());

                row.createCell(13).setCellValue(
                        member.getGotra() == null ? "" : member.getGotra());

                row.createCell(14).setCellValue(
                        member.getPata() == null ? "" : member.getPata());

                row.createCell(15).setCellValue(
                        member.getKuldevi() == null ? "" : member.getKuldevi());

                row.createCell(16).setCellValue(member.getFamily().getFamilyCode());

                row.createCell(17).setCellValue(member.getFamily().getFamilyHeadName());

                row.createCell(18).setCellValue(
                        member.getFamily().getVillage().getVillageName());

                row.createCell(19).setCellValue(
                        member.getFamily().getVillage().getDistrict().getDistrictName());

                row.createCell(20).setCellValue(
                        member.getFamily().getVillage().getDistrict().getState().getStateName());
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            return out.toByteArray();

        } catch (IOException ex) {
            throw new RuntimeException("Failed to export members.", ex);
        }
    }
}