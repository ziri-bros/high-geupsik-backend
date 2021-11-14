package com.highgeupsik.backend.service;


import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.dto.BoardDetailReqDTO;
import com.highgeupsik.backend.dto.UploadFileDTO;
import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Category;
import com.highgeupsik.backend.entity.UploadFile;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.exception.NotMatchException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.UploadFileRepository;
import com.highgeupsik.backend.repository.UserRepository;
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
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return boardRepository.save(Board.builder()
            .user(user)
            .content(content)
            .title(title)
            .category(category)
            .region(user.getSchoolInfo().getRegion())
            .build()).getId();
    }

    public Long savePost(Long userId, BoardDetailReqDTO boardDetailReqDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        Board board = boardRepository.save(Board.builder()
            .user(user)
            .content(boardDetailReqDTO.getContent())
            .title(boardDetailReqDTO.getTitle())
            .category(boardDetailReqDTO.getCategory())
            .region(user.getSchoolInfo().getRegion())
            .thumbnailUrl(boardDetailReqDTO.getUploadFileDTOList().get(0).getFileDownloadUri())
            .build());
        for (UploadFileDTO uploadFileDTO : boardDetailReqDTO.getUploadFileDTOList()) {
            board.setFile(new UploadFile(uploadFileDTO.getFileName(),
                uploadFileDTO.getFileDownloadUri()));
        }
        return board.getId();
    }

    public Long updatePost(Long boardId, BoardDetailReqDTO boardDetailReqDTO) {
        Board board = boardRepository.findById(boardId).orElseThrow(
            () -> new NotFoundException(POST_NOT_FOUND));
        if(!boardDetailReqDTO.getUploadFileDTOList().isEmpty()) {
            board.deleteFiles();
            board.updateBoard(boardDetailReqDTO.getTitle(), boardDetailReqDTO.getContent(),
                boardDetailReqDTO.getUploadFileDTOList().get(0).getFileDownloadUri());
            for (UploadFileDTO uploadFileDTO : boardDetailReqDTO.getUploadFileDTOList()) {
                board.setFile(new UploadFile(uploadFileDTO.getFileName(), uploadFileDTO.getFileDownloadUri()));
            }
        }else{
            board.updateBoard(boardDetailReqDTO.getTitle(), boardDetailReqDTO.getContent());
        }
        return board.getId();
    }

    public void deleteFilesInPost(Long userId, Long postId) {
        Board board = boardRepository.findById(postId).get();
        Long writerId = board.getUser().getId();
        if (userId.equals(writerId)) {
            board.deleteFiles();
            uploadFileRepository.deleteByBoardId(postId);
        } else {
            throw new NotMatchException(WRITER_NOT_MATCH);
        }
    }

    public void deletePost(Long userId, Long postId) {
        Board board = boardRepository.findById(postId).orElseThrow(
            () -> new NotFoundException(POST_NOT_FOUND));
        Long writerId = board.getUser().getId();
        if (userId.equals(writerId)) {
            boardRepository.delete(board);
        } else {
            throw new NotMatchException(WRITER_NOT_MATCH);
        }
    }

}
