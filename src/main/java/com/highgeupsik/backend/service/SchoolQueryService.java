package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.PagingUtils.*;

import com.highgeupsik.backend.dto.SchoolResDTO;
import com.highgeupsik.backend.dto.SchoolSearchCondition;
import com.highgeupsik.backend.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SchoolQueryService {

    private final SchoolRepository schoolRepository;
    private static final int SCHOOL_COUNT = 20;

    public Page<SchoolResDTO> findAllByRegionAndName(Integer pageNum, SchoolSearchCondition condition) {
        return schoolRepository
            .findAllByRegionAndName(condition, orderBySchoolNameAsc(pageNum, SCHOOL_COUNT))
            .map(SchoolResDTO::new);
    }
}
