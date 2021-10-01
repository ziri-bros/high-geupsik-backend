package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.dto.BoardDetailResDTO;
import com.highgeupsik.backend.dto.BoardSearchCondition;
import com.highgeupsik.backend.dto.QBoardDetailResDTO;
import com.highgeupsik.backend.entity.Category;
import com.highgeupsik.backend.entity.Region;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.highgeupsik.backend.entity.QBoardDetail.*;
import static com.highgeupsik.backend.entity.QUploadFile.*;
import static org.springframework.util.StringUtils.isEmpty;


public class BoardDetailRepositoryImpl implements BoardDetailRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardDetailRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardDetailResDTO> findAll(BoardSearchCondition condition, Pageable pageable) {


        OrderSpecifier<LocalDateTime> desc = boardDetail.createdDate.desc();
        List<BoardDetailResDTO> content = queryFactory
                .select(new QBoardDetailResDTO(
                        boardDetail.id, boardDetail.title, boardDetail.content, boardDetail.likeCount,
                        boardDetail.commentCount, boardDetail.createdDate, boardDetail.thumbnail))
                .from(boardDetail)
                .leftJoin(boardDetail.thumbnail, uploadFile)
                .where(
                        regionEq(condition.getRegion()),
                        categoryEq(condition.getCategory()),
                        titleLike(condition.getKeyword()),
                        contentLike(condition.getKeyword()),
                        likeCountGoe(condition.getLikeCount())
                )
                .orderBy(boardDetail.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(boardDetail)
                .where(
                        regionEq(condition.getRegion()),
                        categoryEq(condition.getCategory()),
                        titleLike(condition.getKeyword()),
                        contentLike(condition.getKeyword()),
                        likeCountGoe(condition.getLikeCount()))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression regionEq(Region region) {
        return isEmpty(region) ? null : boardDetail.region.eq(region);

    }

    private BooleanExpression likeCountGoe(Integer likeCount) {
        return likeCount == null ? null : boardDetail.likeCount.goe(likeCount);
    }

    private BooleanExpression categoryEq(Category category) {
        return isEmpty(category) ? null : boardDetail.category.eq(category);
    }

    private BooleanExpression titleLike(String keyword) {
        return isEmpty(keyword) ? null : boardDetail.title.like(keyword);
    }

    private BooleanExpression contentLike(String keyword) {
        return isEmpty(keyword) ? null : boardDetail.content.like(keyword);
    }

}
