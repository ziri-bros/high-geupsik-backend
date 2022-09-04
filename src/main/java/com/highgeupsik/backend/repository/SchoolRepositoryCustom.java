package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.dto.SchoolSearchCondition;
import com.highgeupsik.backend.entity.School;
import java.util.List;

public interface SchoolRepositoryCustom {

    List<School> findAllByRegionAndName(SchoolSearchCondition condition);
}
