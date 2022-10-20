package com.highgeupsik.backend.service.comment;

import com.highgeupsik.backend.api.comment.CommentResDTO;
import com.highgeupsik.backend.repository.comment.CommentRepository;
import com.highgeupsik.backend.utils.PagingUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentQueryService {

    private final CommentRepository commentRepository;

    private final static int COMMENT_COUNT = 20;

    public Page<CommentResDTO> findByMyId(Long userId, Integer pageNum) {
        return commentRepository.findByUserId(userId, PagingUtils.orderByCreatedDateDESC(pageNum, COMMENT_COUNT))
            .map((CommentResDTO::new));
    }
}
