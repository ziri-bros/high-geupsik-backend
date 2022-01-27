package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Board;
import com.highgeupsik.backend.entity.Category;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardResDTO {

    private Long id;
    private Long writerId;
    private String title;
    private String content;
    private String thumbnail;
    private Category category;
    private int likeCount;
    private int commentCount;
    private boolean isUserLike;
    private LocalDateTime createdDate;
    private List<UploadFileDTO> uploadFileDTOList = new ArrayList<>();

    @QueryProjection
    public BoardResDTO(Long id, Long writerId, String title, String content, String thumbnail,
        Category category, int likeCount, int commentCount, LocalDateTime createdDate) {
        this.id = id;
        this.writerId = writerId;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.category = category;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdDate = createdDate;
    }

    public BoardResDTO(Board board, boolean isUserLike) {
        this.id = board.getId();
        this.writerId = board.getUser().getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.category = board.getCategory();
        this.likeCount = board.getLikeCount();
        this.commentCount = board.getCommentCount();
        this.isUserLike = isUserLike;
        this.thumbnail = board.getThumbnail();
        this.uploadFileDTOList = board.getUploadFileList().stream().map((UploadFileDTO::new))
            .collect(Collectors.toList());
        this.createdDate = board.getCreatedDate();
    }

    public BoardResDTO(Board board) {
        this.id = board.getId();
        this.writerId = board.getUser().getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.category = board.getCategory();
        this.likeCount = board.getLikeCount();
        this.commentCount = board.getCommentCount();
        this.thumbnail = board.getThumbnail();
        this.uploadFileDTOList = board.getUploadFileList().stream().map((UploadFileDTO::new))
            .collect(Collectors.toList());
        this.createdDate = board.getCreatedDate();
    }
}
