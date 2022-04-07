package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.dto.SchoolSearchCondition;
import com.highgeupsik.backend.entity.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SchoolRepositoryCustom {

    Page<School> findAllByRegionAndName(SchoolSearchCondition condition, Pageable pageable);
}
