package com.highgeupsik.backend.repository;

import static com.highgeupsik.backend.entity.QSchool.*;
import static org.springframework.util.StringUtils.isEmpty;

import com.highgeupsik.backend.dto.SchoolSearchCondition;
import com.highgeupsik.backend.entity.Region;
import com.highgeupsik.backend.entity.School;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class SchoolRepositoryImpl implements SchoolRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public SchoolRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<School> findAllByRegionAndName(SchoolSearchCondition condition, Pageable pageable) {
        List<School> schools = queryFactory.selectFrom(school)
            .where(regionEq(condition.getRegion()),
                nameContains(condition.getKeyword()))
            .orderBy(school.name.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory.selectFrom(school)
            .where(regionEq(condition.getRegion()),
                nameContains(condition.getKeyword()))
            .fetchCount();

        return new PageImpl<>(schools, pageable, total);
    }

    private BooleanExpression regionEq(Region region) {
        return isEmpty(region) ? null : school.region.eq(region);
    }

    private BooleanExpression nameContains(String name) {
        return isEmpty(name) ? null : school.name.contains(name);
    }
}
