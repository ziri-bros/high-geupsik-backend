package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.UploadFile;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardDetailResDTO {

    private Long id;
    private Long writerId;
    private String title;
    private String content;
    private String thumbnail;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdDate;
    private List<UploadFileDTO> uploadFileDTOList = new ArrayList<>();

    @QueryProjection
    public BoardDetailResDTO(Long id, String title, String content, String thumbnail,
        int likeCount, int commentCount, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdDate = createdDate;

    }

    public BoardDetailResDTO(Board board) {
        id = board.getId();
        title = board.getTitle();
        content = board.getContent();
        likeCount = board.getLikeCount();
        commentCount = board.getCommentCount();
        uploadFileDTOList = board.getUploadFileList().stream().map((uploadFile -> new UploadFileDTO(uploadFile)))
            .collect(Collectors.toList());
    }
}
