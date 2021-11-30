package com.highgeupsik.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.highgeupsik.backend.entity.Comment;

public interface CommentRepositoryCustom {

	Page<Comment> findCommentsBy(Long boardId, Pageable pageable);
}
