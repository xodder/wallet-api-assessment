package com.fundquest.assessment.lib.helpers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

public class PaginatedResult {
    public static <T extends Object> Map<String, Object> from(String key, Page<T> data) {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put(key, data.getContent());
        result.put("pagination", buildPaginationEntry(data));

        return result;
    }

    private static <T extends Object> Map<String, Object> buildPaginationEntry(Page<T> data) {
        Map<String, Object> pagination = new LinkedHashMap<>();

        pagination.put("currentPage", data.getPageable().getPageNumber());
        pagination.put("nextPage", data.hasNext() ? data.nextPageable().getPageNumber() : -1);
        pagination.put("totalPages", data.getTotalPages());
        pagination.put("hasNext", data.hasNext());
        pagination.put("hasPrevious", data.hasPrevious());
        pagination.put("isLast", data.isLast());

        return pagination;
    }
}
