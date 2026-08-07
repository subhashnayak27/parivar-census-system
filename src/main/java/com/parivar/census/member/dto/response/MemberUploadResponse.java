package com.parivar.census.member.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUploadResponse {

    private Integer totalRecords;
    private Integer successRecords;
    private Integer failedRecords;

    private List<String> errors;
}