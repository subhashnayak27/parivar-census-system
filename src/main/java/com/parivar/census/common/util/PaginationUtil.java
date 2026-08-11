package com.parivar.census.common.util;

import com.parivar.census.common.dto.PaginationRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtil {

    private PaginationUtil() {
    }

    public static Pageable getPageable(PaginationRequest request) {

        int page =
                request.getPage() == null
                        ? 0
                        : Math.max(request.getPage(), 0);

        int size =
                request.getSize() == null
                        ? 10
                        : Math.max(request.getSize(), 1);

        String sortBy =
                request.getSortBy() == null || request.getSortBy().isBlank()
                        ? "id"
                        : request.getSortBy();

        String direction =
                request.getDirection() == null || request.getDirection().isBlank()
                        ? "asc"
                        : request.getDirection();

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return PageRequest.of(page, size, sort);
    }
}