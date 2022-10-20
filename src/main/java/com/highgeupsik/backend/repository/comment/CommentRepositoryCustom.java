package com.highgeupsik.backend.repository.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.highgeupsik.backend.entity.board.Comment;

public interface CommentRepositoryCustom {

    Page<Comment> findCommentsBy(Long boardId, Pageable pageable);
}
