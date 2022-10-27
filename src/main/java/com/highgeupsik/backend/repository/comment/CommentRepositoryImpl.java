package com.highgeupsik.backend.repository.comment;

import static com.highgeupsik.backend.entity.board.QComment.comment;

import com.highgeupsik.backend.entity.board.Comment;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findCommentsBy(Long boardId, Pageable pageable) {
        List<Comment> comments = queryFactory.selectFrom(comment)
            .where(comment.board.id.eq(boardId))
            .orderBy(comment.parent.id.asc(), comment.id.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> commentCount = queryFactory
            .select(comment.count())
            .where(comment.board.id.eq(boardId));

        return PageableExecutionUtils.getPage(comments, pageable, commentCount::fetchOne);
    }
}
