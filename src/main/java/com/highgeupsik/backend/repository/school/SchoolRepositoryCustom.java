package com.highgeupsik.backend.repository.school;

import com.highgeupsik.backend.api.school.SchoolSearchCondition;
import com.highgeupsik.backend.entity.school.School;
import java.util.List;

public interface SchoolRepositoryCustom {

    List<School> findAllByRegionAndName(SchoolSearchCondition condition);
}
