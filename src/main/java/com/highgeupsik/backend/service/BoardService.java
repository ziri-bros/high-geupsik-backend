package com.highgeupsik.backend.service;


import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.dto.BoardReqDTO;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final UploadFileRepository uploadFileRepository;

    public Long saveBoard(Long userId, String title, String content, Category category) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException(USER_NOT_FOUND));
        return boardRepository.save(Board.builder()
            .user(user)
            .content(content)
            .title(title)
            .category(category)
            .region(user.getSchool().getRegion())
            .build()).getId();
    }

    public Long saveBoard(Long userId, BoardReqDTO boardReqDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        Board board = boardRepository.save(Board.builder()
            .user(user)
            .content(boardReqDTO.getContent())
            .title(boardReqDTO.getTitle())
            .category(boardReqDTO.getCategory())
            .region(user.getSchool().getRegion())
            .thumbnail(boardReqDTO.getUploadFileDTOList().get(0).getFileDownloadUri())
            .build());
        for (UploadFileDTO uploadFileDTO : boardReqDTO.getUploadFileDTOList()) {
            board.setFile(new UploadFile(uploadFileDTO.getFileName(),
                uploadFileDTO.getFileDownloadUri()));
        }
        return board.getId();
    }

    public Long updateBoard(Long boardId, BoardReqDTO boardReqDTO) {
        Board board = boardRepository.findById(boardId).orElseThrow(
            () -> new NotFoundException(POST_NOT_FOUND));
        if(!boardReqDTO.getUploadFileDTOList().isEmpty()) {
            board.deleteFiles();
            board.updateBoard(boardReqDTO.getTitle(), boardReqDTO.getContent(),
                boardReqDTO.getUploadFileDTOList().get(0).getFileDownloadUri());
            for (UploadFileDTO uploadFileDTO : boardReqDTO.getUploadFileDTOList()) {
                board.setFile(new UploadFile(uploadFileDTO.getFileName(), uploadFileDTO.getFileDownloadUri()));
            }
        }else{
            board.updateBoard(boardReqDTO.getTitle(), boardReqDTO.getContent());
        }
        return board.getId();
    }

    public void deleteFilesInBoard(Long userId, Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        Long writerId = board.getUser().getId();
        if (userId.equals(writerId)) {
            board.deleteFiles();
            uploadFileRepository.deleteByBoardId(boardId);
        } else {
            throw new NotMatchException(WRITER_NOT_MATCH);
        }
    }

    public void deleteBoard(Long userId, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
            () -> new NotFoundException(POST_NOT_FOUND));
        Long writerId = board.getUser().getId();
        if (userId.equals(writerId)) {
            boardRepository.delete(board);
        } else {
            throw new NotMatchException(WRITER_NOT_MATCH);
        }
    }

}
