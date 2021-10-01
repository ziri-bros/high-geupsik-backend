package com.highgeupsik.backend.service;


import com.highgeupsik.backend.entity.BoardDetail;
import com.highgeupsik.backend.entity.UploadFile;
import com.highgeupsik.backend.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadFileService {

    private final UploadFileRepository uploadFileRepository;

    public void saveFiles(List<UploadFile> uploadFileList, BoardDetail boardDetail) {
        for(UploadFile file : uploadFileList) {
            file.setBoardDetail(boardDetail);
            uploadFileRepository.save(file);
        }
    }

}
