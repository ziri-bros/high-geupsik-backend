package com.highgeupsik.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {

	private final CommentRepository commentRepository;

	private static final int COMMENT_COUNT = 20;

	public int findUserCountByUserIdAndBoardId(Long userId, Long postId) { //익명번호 조회
		List<Comment> comments = commentRepository.findByUserIdAndBoardId(userId, postId);
		return comments.isEmpty() ? 0 : comments.get(0).getAnonymousId();
	}

}
