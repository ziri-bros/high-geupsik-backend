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

    public BoardResDTO(Board board) {
        id = board.getId();
        writerId = board.getUser().getId();
        title = board.getTitle();
        content = board.getContent();
        category = board.getCategory();
        likeCount = board.getLikeCount();
        commentCount = board.getCommentCount();
        uploadFileDTOList = board.getUploadFileList().stream().map((uploadFile -> new UploadFileDTO(uploadFile)))
            .collect(Collectors.toList());
        createdDate = board.getCreatedDate();
    }
}
