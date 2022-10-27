package com.highgeupsik.backend.repository.school;

import static com.highgeupsik.backend.entity.school.QSchool.school;
import static org.springframework.util.ObjectUtils.isEmpty;

import com.highgeupsik.backend.api.school.SchoolSearchCondition;
import com.highgeupsik.backend.entity.school.Region;
import com.highgeupsik.backend.entity.school.School;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SchoolRepositoryImpl implements SchoolRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<School> findAllByRegionAndName(SchoolSearchCondition condition) {
        return queryFactory.selectFrom(school)
            .where(regionEq(condition.getRegion()),
                nameContains(condition.getKeyword()))
            .orderBy(school.name.asc())
            .fetch();
    }

    private BooleanExpression regionEq(Region region) {
        return isEmpty(region) ? null : school.region.eq(region);
    }

    private BooleanExpression nameContains(String name) {
        return isEmpty(name) ? null : school.name.contains(name);
    }
}
