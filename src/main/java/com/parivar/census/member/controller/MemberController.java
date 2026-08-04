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

import java.util.List;

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

        MemberResponse response = memberService.createMember(request);

        return ApiResponse.<MemberResponse>builder()
                .success(true)
                .message("Member created successfully")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<MemberResponse> getMemberById(@PathVariable Long id) {

        log.info("Received request to fetch member with id: {}", id);

        MemberResponse response = memberService.getMemberById(id);

        return ApiResponse.<MemberResponse>builder()
                .success(true)
                .message("Member fetched successfully")
                .data(response)
                .build();
    }
    @GetMapping
    public ApiResponse<List<MemberResponse>> getAllMembers() {
        log.info("Received request to fetch members");
        List<MemberResponse> response = memberService.getAllMembers();
        return ApiResponse.<List<MemberResponse>>builder().success(true).message("Members fetch successfully")
                .data(response).build();
    }

    @PutMapping("/{id}")
    public ApiResponse<MemberResponse> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequest request) {

        log.info("Received request to update member with id {}", id);

        MemberResponse response = memberService.updateMember(id, request);

        return ApiResponse.<MemberResponse>builder()
                .success(true)
                .message("Member updated successfully")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMember(@PathVariable Long id) {

        log.info("Received request to delete member with id {}", id);

        memberService.deleteMember(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Member deleted successfully")
                .build();
    }

}