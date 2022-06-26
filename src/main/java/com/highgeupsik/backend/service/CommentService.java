package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.entity.NotificationType;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.highgeupsik.backend.dto.CommentReqDTO;
import com.highgeupsik.backend.dto.CommentResDTO;
import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Comment;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.CommentRepository;
import com.highgeupsik.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public CommentResDTO saveComment(Long userId, Long boardId, CommentReqDTO dto) {
        User commentWriter = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new ResourceNotFoundException(BOARD_NOT_FOUND));

        int anonymousNumber = getAnonymousNumberFrom(board, commentWriter);
        Comment comment = Comment.of(dto.getContent(), commentWriter, board);
        comment.setBoard(board);
        comment.setAnonymousId(anonymousNumber);
        if (board.isWriter(commentWriter.getId())) {
            comment.setAnonymousId(-1);
        }
        commentRepository.save(comment);

        Set<Long> sendUserIdList = new HashSet<>();
        if (dto.getParentId() != null) {
            Comment parent = commentRepository.findById(dto.getParentId())
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
            setSendIdList(sendUserIdList, comment, parent);
            saveReplyNotification(sendUserIdList, board, comment);
            transformToReply(comment, parent);
        }
        saveCommentNotification(board, comment, sendUserIdList);

        return new CommentResDTO(comment, false);
    }

    public void saveReplyNotification(Set<Long> sendUserIdList, Board board, Comment comment) {
        for (Long userId : sendUserIdList) {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
            notificationService.saveCommentNotification(user, board, comment, NotificationType.REPLY);
        }
    }

    public void saveCommentNotification(Board board, Comment comment, Set<Long> sendUserIdList) {
        if (!board.isWriter(comment.getUser().getId()) && !sendUserIdList.contains(board.getUser().getId())) {
            User boardWriter = userRepository.findById(board.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
            notificationService.saveCommentNotification(boardWriter, board, comment, NotificationType.COMMENT);
        }
    }

    public int getAnonymousNumberFrom(Board board, User writer) {
        return commentRepository.findFirstByBoardAndUser(board, writer)
            .map(Comment::getAnonymousId)
            .orElseGet(board::getNextAnonymousNumber);
    }

    public void transformToReply(Comment comment, Comment parent) {
        comment.toReply(parent);
    }

    public Long updateComment(Long userId, Long commentId, CommentReqDTO commentReqDTO) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
        comment.checkWriter(userId);
        comment.updateContent(commentReqDTO);
        return commentId;
    }

    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
        Comment parent = comment.getParent();

        comment.checkWriter(userId);
        comment.disable();
        if (comment.isReply()) {
            parent.deleteReply();
            parent.deleteIfCan();
        } else {
            comment.deleteIfCan();
        }
        Board board = comment.getBoard();
        board.deleteCommentCount();
    }

    public void setSendIdList(Set<Long> sendUserIdList, Comment newComment, Comment parent) {
        Long newCommentWriterId = newComment.getUser().getId();
        Long parentCommentWriterId = parent.getUser().getId();
        sendForParent(newCommentWriterId, parentCommentWriterId, sendUserIdList);
        sendForChildren(parent, newCommentWriterId, sendUserIdList);
    }

    public void sendForParent(Long newCommentWriterId, Long parentCommentWriterId, Set<Long> sendUserIdList) {
        if (isNewCommentWriterNotEqualsPresentCommentWriter(newCommentWriterId, parentCommentWriterId)) {
            sendUserIdList.add(parentCommentWriterId);
        }
    }

    public void sendForChildren(Comment parent, Long newCommentWriterId, Set<Long> sendUserIdList) {
        sendUserIdList.addAll(parent.getChildren().stream()
            .filter(c -> isNewCommentWriterNotEqualsPresentCommentWriter(c.getUser().getId(), newCommentWriterId))
            .map(comment -> comment.getUser().getId())
            .collect(Collectors.toList()));
    }

    public boolean isNewCommentWriterNotEqualsPresentCommentWriter(Long writerId, Long commentWriterId) {
        return !writerId.equals(commentWriterId);
    }
}
