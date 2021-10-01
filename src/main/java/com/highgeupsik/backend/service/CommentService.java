package com.highgeupsik.backend.service;

import com.highgeupsik.backend.dto.CommentReqDTO;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.BoardDetail;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.exception.NotMatchException;
import com.highgeupsik.backend.repository.CommentRepository;
import com.highgeupsik.backend.repository.BoardDetailRepository;
import com.highgeupsik.backend.repository.UserRepository;
import com.highgeupsik.backend.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardDetailRepository boardDetailRepository;
    private final UserRepository userRepository;



    public Long saveComment(Long userId, String content, Long postId) {
        BoardDetail boardDetail = boardDetailRepository.findById(postId).orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
        boardDetail.updateBoardCommentCount(true);
        boardDetail.updateBoardUserCount();
        Comment comment = commentRepository.save(Comment.builder()
                .user(user)
                .userCount(boardDetail.getUserCount())
                .content(content)
                .build());
        comment.setBoardDetail(boardDetail);
        return comment.getId();
    }

    public Long saveComment(Long userId, String content, Long postId, int userCount) {
        BoardDetail boardDetail = boardDetailRepository.findById(postId).orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
        boardDetail.updateBoardCommentCount(true);
        Comment comment = commentRepository.save(Comment.builder()
                .user(user)
                .userCount(userCount)
                .content(content)
                .build());
        comment.setBoardDetail(boardDetail);
        return comment.getId();
    }

    public Long saveReplyComment(Long userId, String content, Long boardId, Long postId) {
        BoardDetail boardDetail = boardDetailRepository.findById(postId).orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        Comment parent = commentRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
        boardDetail.updateBoardCommentCount(true);
        boardDetail.updateBoardUserCount();
        if (parent.getParent() != null) {
            parent = parent.getParent();
        }
        Comment now = commentRepository.save(Comment.builder()
                .user(userRepository.findById(userId).get())
                .userCount(boardDetail.getUserCount())
                .boardDetail(boardDetail)
                .content(content)
                .build());
        now.setParent(parent);
        return now.getId();
    }

    public Long saveReplyComment(Long userId, String content, Long parentId, Long boardId, int userCount) {
        BoardDetail boardDetail = boardDetailRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        Comment parent = commentRepository.findById(parentId).orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
        boardDetail.updateBoardCommentCount(true);
        if (parent.getParent() != null) {
            parent = parent.getParent();
        }
        Comment now = commentRepository.save(Comment.builder()
                .user(userRepository.findById(userId).get())
                .userCount(userCount)
                .boardDetail(boardDetail)
                .content(content)
                .build());
        now.setParent(parent);
        return now.getId();
    }

    public Long updateComment(Long userId, Long commentId, CommentReqDTO commentReqDTO) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
        Long writerId = comment.getUser().getId();
        if (userId.equals(writerId)) {
            comment.updateContent(commentReqDTO);
            return commentId;
        }
        throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
    }

    public void deleteComment(Long userId, Long commentId, Long boardId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
        Long writerId = comment.getUser().getId();
        if (userId.equals(writerId)) {
            comment.delete();
            boardDetailRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND))
                    .updateBoardCommentCount(false);
        } else {
            throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
        }
    }

}
