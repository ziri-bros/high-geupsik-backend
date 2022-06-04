package com.highgeupsik.backend.service;

import static java.util.stream.Collectors.*;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.highgeupsik.backend.dto.CommentResDTO;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.repository.CommentRepository;
import com.highgeupsik.backend.repository.LikeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardCommentService {

    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    @Transactional(readOnly = true)
    public Page<CommentResDTO> findCommentsBy(Long userId, Long boardId, Integer pageNum, Integer pageSize) {
        Page<Comment> comments = commentRepository.findCommentsBy(boardId, PageRequest.of(pageNum - 1, pageSize));
        Set<Long> likes = getUserLikeSet(userId, comments);
        return comments.map(comment -> new CommentResDTO(comment, likes.contains(comment.getId())));
    }

    private Set<Long> getUserLikeSet(Long userId, Page<Comment> comments) {
        return likeRepository.findAllByUserIdAndCommentIn(userId, comments.getContent())
            .stream()
            .map(like -> like.getComment().getId())
            .collect(toSet());
    }
}
