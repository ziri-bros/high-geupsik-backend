package com.highgeupsik.backend.service;


import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Category;
import com.highgeupsik.backend.entity.UploadFile;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.exception.NotMatchException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.UploadFileRepository;
import com.highgeupsik.backend.repository.UserRepository;
import com.highgeupsik.backend.utils.ErrorMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardDetailService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final UploadFileRepository uploadFileRepository;

    public Long savePost(Long userId, String title, String content, Category category) {
        User user = userRepository.findById(userId).get();
        return boardRepository.save(Board.builder()
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
        Board board = boardRepository.save(Board.builder()
            .user(user)
            .content(content)
            .title(title)
            .category(category)
            .region(user.getSchoolInfo().getRegion())
            .thumbnail(uploadFileList.get(0))
            .build());
        for (UploadFile file : uploadFileList) {
            board.setFile(file);
        }
        return board.getId();
    }

    public Long updatePost(Long userId, Long postId, String title, String content) {
        Board board = boardRepository.findById(postId).orElseThrow(
            () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        Long writerId = board.getUser().getId();
        if (userId.equals(writerId)) {
            return board.getId();
        } else {
            throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
        }
    }

    public Long updatePost(Long userId, Long postId, String title, String content,
        List<UploadFile> uploadFileList) {
        Board board = boardRepository.findById(postId).orElseThrow(
            () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        Long writerId = board.getUser().getId();
        if (userId.equals(writerId)) {
            for (UploadFile uploadFile : uploadFileList) {
                uploadFile.setBoard(board);
            }
            return board.getId();
        } else {
            throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
        }
    }

    public void deleteFilesInPost(Long userId, Long postId) {
        Board board = boardRepository.findById(postId).get();
        Long writerId = board.getUser().getId();
        if (userId.equals(writerId)) {
            board.deleteFiles();
            uploadFileRepository.deleteByBoardId(postId);
        } else {
            throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
        }
    }

    public void deletePost(Long userId, Long postId) {
        Board board = boardRepository.findById(postId).orElseThrow(
            () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND));
        Long writerId = board.getUser().getId();
        if (userId.equals(writerId)) {
            boardRepository.delete(board);
        } else {
            throw new NotMatchException(ErrorMessage.WRITER_NOT_MATCH);
        }
    }

}
