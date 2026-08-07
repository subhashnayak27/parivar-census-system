package com.parivar.census.common.util;

import com.parivar.census.common.dto.PaginationRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PaginationUtil {

    private PaginationUtil() {
    }

    public static Pageable getPageable(PaginationRequest request) {

        Sort.Direction direction =
                request.getDirection().equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, request.getSortBy());

        return PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort
        );
    }
}