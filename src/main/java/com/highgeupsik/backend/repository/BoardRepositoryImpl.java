package com.highgeupsik.backend.repository;

import static com.highgeupsik.backend.entity.QBoard.*;
import static org.springframework.util.StringUtils.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.highgeupsik.backend.dto.BoardResDTO;
import com.highgeupsik.backend.dto.BoardSearchCondition;
import com.highgeupsik.backend.dto.QBoardResDTO;
import com.highgeupsik.backend.entity.Category;
import com.highgeupsik.backend.entity.Region;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardResDTO> findAll(BoardSearchCondition condition, Pageable pageable) {

        OrderSpecifier<LocalDateTime> desc = board.createdDate.desc();
        List<BoardResDTO> content = queryFactory
            .select(new QBoardResDTO(board.id, board.user.id, board.title, board.content,
                board.thumbnail, board.category, board.likeCount, board.commentCount, board.createdDate))
            .from(board)
            .where(
                regionEq(condition.getRegion()),
                categoryEq(condition.getCategory()),
                titleLike(condition.getKeyword()),
                contentLike(condition.getKeyword()),
                likeCountGoe(condition.getLikeCount())
            )
            .orderBy(board.createdDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory
            .selectFrom(board)
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
        return isEmpty(region) ? null : board.region.eq(region);
    }

    private BooleanExpression likeCountGoe(Integer likeCount) {
        return likeCount == null ? null : board.likeCount.goe(likeCount);
    }

    private BooleanExpression categoryEq(Category category) {
        return isEmpty(category) ? null : board.category.eq(category);
    }

    private BooleanExpression titleLike(String keyword) {
        return isEmpty(keyword) ? null : board.title.like(keyword);
    }

    private BooleanExpression contentLike(String keyword) {
        return isEmpty(keyword) ? null : board.content.like(keyword);
    }

}
