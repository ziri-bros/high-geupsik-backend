package com.highgeupsik.backend.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PagingUtils {

    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final int MESSAGE_COUNT = 20;

    public static PageRequest orderByCreatedDateDESC(Integer pageNum, int count) {
        return PageRequest.of(pageNum, count, Sort.by(Sort.Direction.DESC, "createdDate"));
    }

    public static PageRequest orderByCreatedDateASC(Integer pageNum, int count) {
        return PageRequest.of(pageNum, count, Sort.by(Sort.Direction.ASC, "createdDate"));
    }

    public static PageRequest orderByModifiedDate(Integer pageNum, int count) {
        return PageRequest.of(pageNum, count, Sort.by(Sort.Direction.DESC, "modifiedDate"));
    }
}
