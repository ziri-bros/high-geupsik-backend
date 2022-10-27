package com.highgeupsik.backend.repository.board;

import static com.highgeupsik.backend.entity.board.QBoard.board;
import static org.springframework.util.StringUtils.hasText;

import com.highgeupsik.backend.api.board.BoardResDTO;
import com.highgeupsik.backend.api.board.BoardSearchCondition;
import com.highgeupsik.backend.api.board.QBoardResDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardResDTO> findAll(BoardSearchCondition condition, Pageable pageable) {

        BooleanBuilder builder = getBuilder(condition);

        List<BoardResDTO> content = queryFactory
            .select(new QBoardResDTO(board.id, board.user.id, board.title, board.content,
                board.thumbnail, board.category, board.likeCount, board.commentCount, board.createdDate))
            .from(board)
            .where(builder)
            .orderBy(board.createdDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> total = queryFactory
            .select(board.count())
            .from(board)
            .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
    }

    private BooleanBuilder getBuilder(BoardSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(board.deleteFlag.eq(false));
        if (hasText(condition.getKeyword())) {
            builder.and(board.title.contains(condition.getKeyword())
                .or(board.content.contains(condition.getKeyword())));
        }
        if (hasText(condition.getRegion().toString())) {
            builder.and(board.region.eq(condition.getRegion()));
        }
        if (condition.getCategory() != null && hasText(condition.getCategory().toString())) {
            builder.and(board.category.eq(condition.getCategory()));
        }
        if (condition.getLikeCount() != null) {
            builder.and(board.likeCount.goe(condition.getLikeCount()));
        }
        return builder;
    }
}
