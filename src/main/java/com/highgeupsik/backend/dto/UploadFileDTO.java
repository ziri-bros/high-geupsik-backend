package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.UploadFile;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UploadFileDTO {

    private Long id;
    private String fileName;
    private String fileDownloadUri;

    public UploadFileDTO(UploadFile uploadFile) {
        id = uploadFile.getId();
        fileName = uploadFile.getFileName();
        fileDownloadUri = uploadFile.getFileDownloadUri();
    }
}
