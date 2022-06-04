package com.highgeupsik.backend.repository;

import static com.highgeupsik.backend.entity.QComment.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.highgeupsik.backend.entity.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Comment> findCommentsBy(Long boardId, Pageable pageable) {
        List<Comment> comments = queryFactory.selectFrom(comment)
            .where(comment.board.id.eq(boardId))
            .orderBy(comment.parent.id.asc(), comment.id.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long commentCount = queryFactory.selectFrom(comment)
            .where(comment.board.id.eq(boardId))
            .fetchCount();

        return new PageImpl<>(comments, pageable, commentCount);
    }
}
