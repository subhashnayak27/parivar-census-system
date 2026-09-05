package com.parivar.census.member.controller;

import com.parivar.census.common.dto.PageResponse;
import com.parivar.census.common.dto.PaginationRequest;
import com.parivar.census.common.ApiResponse;
import com.parivar.census.member.dto.request.MemberRequest;
import com.parivar.census.member.dto.response.MemberResponse;
import com.parivar.census.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY')")
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
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY','VIEWER')")
    public ApiResponse<PageResponse<MemberResponse>> searchMembers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        log.info(
                "Searching members. keyword={}, page={}, size={}, sortBy={}, direction={}",
                keyword,
                page,
                size,
                sortBy,
                direction
        );

        PaginationRequest request = new PaginationRequest();

        request.setPage(page);
        request.setSize(size);
        request.setSortBy(sortBy);
        request.setDirection(direction);

        return ApiResponse.success(
                "Members fetched successfully",
                memberService.searchMembers(keyword, request)
        );
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY','VIEWER')")
    public ApiResponse<MemberResponse> getMemberById(@PathVariable Long id) {

        log.info("Received request to fetch member with id: {}", id);

        MemberResponse response = memberService.getMemberById(id);

        return ApiResponse.<MemberResponse>builder()
                .success(true)
                .message("Member fetched successfully")
                .data(response)
                .build();
    }

    @GetMapping("/family/{familyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY','VIEWER')")
    public ApiResponse<java.util.List<MemberResponse>> getMembersByFamilyId(@PathVariable Long familyId) {
        log.info("Received request to fetch members for family id: {}", familyId);

        return ApiResponse.<java.util.List<MemberResponse>>builder()
                .success(true)
                .message("Members fetched successfully")
                .data(memberService.getMembersByFamilyId(familyId))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY')")
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
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ApiResponse<Void> deleteMember(@PathVariable Long id) {

        log.info("Received request to delete member with id {}", id);

        memberService.deleteMember(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Member deleted successfully")
                .build();
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','DATA_ENTRY','VIEWER')")
    @GetMapping
    public ApiResponse<PageResponse<MemberResponse>> getAllMembers(
            @ParameterObject
            @ModelAttribute
            PaginationRequest request) {

        return ApiResponse.success(
                "Members fetched successfully",
                memberService.getAllMembers(request));
    }
}