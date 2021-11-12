package com.highgeupsik.backend.service;


import static com.highgeupsik.backend.utils.PagingUtils.orderByCreatedDateASC;
import static com.highgeupsik.backend.utils.PagingUtils.orderByCreatedDateDESC;

import com.highgeupsik.backend.dto.CommentResDTO;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.repository.CommentRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {

    private final CommentRepository commentRepository;

    private static final int COMMENT_COUNT = 20;

    public List<CommentResDTO> findByBoardId(Long postId, Integer pageNum) { //게시글에 있는 댓글
        return commentRepository.findByBoardIdAndParent(postId, null,
                orderByCreatedDateASC(pageNum, COMMENT_COUNT)).getContent()
            .stream().map((comment) -> new CommentResDTO(comment)).collect(Collectors.toList());
    }

    public List<CommentResDTO> findByMyId(Long userId, Integer pageNum) { //내가 작성한 댓글
        return commentRepository.findByUserId(userId, orderByCreatedDateDESC(pageNum, COMMENT_COUNT)).getContent()
            .stream().map((comment) -> new CommentResDTO(comment)).collect(Collectors.toList());
    }

    public int findUserCountByUserIdAndBoardId(Long userId, Long postId) { //익명번호 조회
        List<Comment> comments = commentRepository.findByUserIdAndBoardId(userId, postId);
        return comments.isEmpty() ? 0 : comments.get(0).getUserCount();
    }

}
