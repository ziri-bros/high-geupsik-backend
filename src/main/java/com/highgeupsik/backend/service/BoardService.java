package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.dto.BoardReqDTO;
import com.highgeupsik.backend.dto.UploadFileDTO;
import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Category;
import com.highgeupsik.backend.entity.UploadFile;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.BoardRepository;
import com.highgeupsik.backend.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public Board saveBoard(Long userId, String title, String content, Category category) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException(USER_NOT_FOUND));
        return boardRepository.save(Board.builder()
            .user(user)
            .content(content)
            .title(title)
            .category(category)
            .region(user.getSchool().getRegion())
            .build());
    }

    public Long makeBoard(Long userId, BoardReqDTO boardReqDTO) {
        Board board = saveBoard(userId, boardReqDTO.getTitle(), boardReqDTO.getContent(), boardReqDTO.getCategory());
        if (!boardReqDTO.getUploadFileDTOList().isEmpty()) {
            addUploadFiles(board, boardReqDTO.getUploadFileDTOList());
        }
        return board.getId();
    }

    public Long modifyBoard(Long userId, Long boardId, BoardReqDTO boardReqDTO) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(BOARD_NOT_FOUND));
        board.checkWriter(userId);
        board.deleteFiles();
        if (!boardReqDTO.getUploadFileDTOList().isEmpty()) {
            addUploadFiles(board, boardReqDTO.getUploadFileDTOList());
        }
        board.updateBoard(boardReqDTO.getTitle(), boardReqDTO.getContent(), boardReqDTO.getCategory());
        return board.getId();
    }

    public void removeBoard(Long userId, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
            () -> new NotFoundException(BOARD_NOT_FOUND));
        board.checkWriter(userId);
        boardRepository.delete(board);
    }

    public void addUploadFiles(Board board, List<UploadFileDTO> uploadFileDTOList) {
        board.setThumbnail(uploadFileDTOList.get(0).getFileDownloadUri());
        for (UploadFileDTO uploadFileDTO : uploadFileDTOList) {
            board.setFile(new UploadFile(uploadFileDTO.getFileName(), uploadFileDTO.getFileDownloadUri()));
        }
    }
}
