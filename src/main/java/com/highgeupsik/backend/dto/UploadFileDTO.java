package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.UploadFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UploadFileDTO {

    private String fileName;
    private String fileDownloadUri;

    public UploadFileDTO(UploadFile uploadFile) {
        fileName = uploadFile.getFileName();
        fileDownloadUri = uploadFile.getFileDownloadUri();
    }

    public UploadFileDTO(String fileName, String fileDownloadUri) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
    }
}
