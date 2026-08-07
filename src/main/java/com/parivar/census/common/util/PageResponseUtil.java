package com.parivar.census.common.util;

import com.parivar.census.common.dto.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public final class PageResponseUtil {

    private PageResponseUtil() {
    }

    public static <E, D> PageResponse<D> of(
            Page<E> page,
            List<D> content) {

        return PageResponse.<D>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}