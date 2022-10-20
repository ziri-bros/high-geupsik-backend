package com.highgeupsik.backend.entity;

import com.highgeupsik.backend.entity.board.Board;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UploadFile {

    @Id
    @GeneratedValue
    @Column(name = "upload_file_id")
    private Long id;

    private String fileName;

    private String fileDownloadUri;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public UploadFile(String fileName, String fileDownloadUri) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
    }

    public void setBoard(Board board) {
        this.board = board;
        if (!board.getUploadFileList().contains(this)) {
            board.getUploadFileList().add(this);
        }
    }
}
