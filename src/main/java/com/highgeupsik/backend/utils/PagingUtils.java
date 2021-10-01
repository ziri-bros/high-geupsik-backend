package com.highgeupsik.backend.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PagingUtils {

    public static PageRequest orderByCreatedDateDESC(Integer pageNum, int count) {
        return PageRequest.of(pageNum - 1, count, Sort.by(Sort.Direction.DESC, "createdDate"));
    }

    public static PageRequest orderByCreatedDateASC(Integer pageNum, int count) {
        return PageRequest.of(pageNum - 1, count, Sort.by(Sort.Direction.ASC, "createdDate"));
    }

    public static PageRequest orderByUserName(Integer pageNum, int count) {
        return PageRequest.of(pageNum - 1, count, Sort.by(Sort.Direction.ASC, "username"));
    }

    public static PageRequest orderByModifiedDate(Integer pageNum, int count) {
        return PageRequest.of(pageNum - 1, count, Sort.by(Sort.Direction.DESC, "modifiedDate"));
    }

}
