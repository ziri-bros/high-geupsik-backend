package com.highgeupsik.backend.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class UploadFile {

    @Id
    @GeneratedValue
    @Column(name = "upload_file_id")
    private Long id;

    private String fileName;

    private String fileDownloadUri;

    @JoinColumn(name = "board_detail_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardDetail boardDetail;

    @Builder
    public UploadFile(String fileName, String fileDownloadUri) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
    }

    public void setBoardDetail(BoardDetail boardDetail){
        this.boardDetail = boardDetail;
        if(!boardDetail.getUploadFileList().contains(this)){
            boardDetail.getUploadFileList().add(this);
        }
    }

}
