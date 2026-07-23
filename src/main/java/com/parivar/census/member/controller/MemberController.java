package com.parivar.census.member.controller;

import com.parivar.census.common.ApiResponse;
import com.parivar.census.member.dto.request.MemberRequest;
import com.parivar.census.member.dto.response.MemberResponse;
import com.parivar.census.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MemberResponse> createMember(
            @Valid @RequestBody MemberRequest request) {

        log.info("Received request to create member with code: {}",
                request.getMemberCode());

        MemberResponse response = memberService.createMember(request);

        return ApiResponse.<MemberResponse>builder()
                .success(true)
                .message("Member created successfully")
                .data(response)
                .build();
    }
}