package com.highgeupsik.backend.service;


import com.highgeupsik.backend.entity.BoardDetail;
import com.highgeupsik.backend.entity.Category;
import com.highgeupsik.backend.entity.UploadFile;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.exception.NotMatchException;
import com.highgeupsik.backend.repository.BoardDetailRepository;
import com.highgeupsik.backend.repository.UploadFileRepository;
import com.highgeupsik.backend.repository.UserRepository;
import com.highgeupsik.backend.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardDetailService {

    private final BoardDetailRepository boardDetailRepository;
    private final UserRepository userRepository;
    private final UploadFileRepository uploadFileRepository;

    public Long savePost(Long userId, String title, String content, Category category) {
        User user = userRepository.findById(userId).get();
        return boardDetailRepository.save(BoardDetail.builder()
                .user(user)
                .content(content)
                .title(title)
                .category(category)
                .region(user.getSchoolInfo().getRegion())
                .build()).getId();
    }

    public Long savePost(Long userId, String title, String content, Category category,
                         List<UploadFile> uploadFileList) {
        User user = userRepository.findById(userId).get();
        BoardDetail boardDetail = boardDetailRepository.save(BoardDetail.builder()
                .user(user)
                .content(content)
                .title(title)
                .category(category)
                .region(user.getSchoolInfo().getRegion())
                .thumbnail(uploadFileList.get(0))
                .build());
        for (UploadFile file : uploadFileList) {
            boardDetail.setFile(file);
        }
        return boardDetail.getId();
    }

    public Long updatePost(Long userId, Long postId, String title, String content) {
        BoardDetail boardDetail = boardDetailRepository.findById(postId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        Long writerId = boardDetail.getUser().getId();
        if (userId.equals(writerId)) {
            return boardDetail.getId();
        } else {
            throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
        }
    }

    public Long updatePost(Long userId, Long postId, String title, String content,
                           List<UploadFile> uploadFileList) {
        BoardDetail boardDetail = boardDetailRepository.findById(postId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        Long writerId = boardDetail.getUser().getId();
        if (userId.equals(writerId)) {
            for (UploadFile uploadFile : uploadFileList) {
                uploadFile.setBoardDetail(boardDetail);
            }
            return boardDetail.getId();
        } else {
            throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
        }
    }

    public void updatePostLike(Boolean flag, Long postId) {
        BoardDetail boardDetail = boardDetailRepository.findById(postId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        boardDetail.updateBoardLikeCount(flag);
    }

    public void updatePostUserCount(Long postId) {
        BoardDetail boardDetail = boardDetailRepository.findById(postId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        boardDetail.updateBoardUserCount();
    }

    public void updatePostCommentCount(Long postId, Boolean flag) {
        BoardDetail boardDetail = boardDetailRepository.findById(postId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
    }

    public void addFilesInPost(Long postId, List<UploadFile> uploadFileList) {
        BoardDetail boardDetail = boardDetailRepository.findById(postId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        for (UploadFile file : uploadFileList) {
            boardDetail.setFile(file);
        }
    }

    public void deleteFilesInPost(Long userId, Long postId) {
        BoardDetail boardDetail = boardDetailRepository.findById(postId).get();
        Long writerId = boardDetail.getUser().getId();
        if (userId.equals(writerId)) {
            boardDetail.deleteFiles();
            uploadFileRepository.deleteByBoardDetailId(postId);
        } else {
            throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
        }
    }

    public void deletePost(Long userId, Long postId) {
        BoardDetail boardDetail = boardDetailRepository.findById(postId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        Long writerId = boardDetail.getUser().getId();
        if (userId.equals(writerId)) {
            boardDetailRepository.delete(boardDetail);
        } else {
            throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
        }
    }

}
