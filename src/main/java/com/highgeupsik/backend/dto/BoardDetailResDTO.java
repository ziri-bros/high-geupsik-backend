package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.BoardDetail;
import com.highgeupsik.backend.entity.UploadFile;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class BoardDetailResDTO {

    private Long id;
    private Long writerId;
    private String title;
    private String content;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdDate;
    private UploadFileDTO thumbnail;
    private List<UploadFileDTO> uploadFileDTOList = new ArrayList<>();

    @QueryProjection
    public BoardDetailResDTO(Long id, String title, String content, int likeCount, int commentCount,
                             LocalDateTime createdDate, UploadFile thumbnail) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdDate = createdDate;
        if(thumbnail != null)
            this.thumbnail = new UploadFileDTO(thumbnail);
    }

    public BoardDetailResDTO(BoardDetail boardDetail){
        id = boardDetail.getId();
        title = boardDetail.getTitle();
        content = boardDetail.getContent();
        likeCount = boardDetail.getLikeCount();
        commentCount = boardDetail.getCommentCount();
        uploadFileDTOList = boardDetail.getUploadFileList().stream().map((uploadFile -> new UploadFileDTO(uploadFile)))
                .collect(Collectors.toList());
    }
}
