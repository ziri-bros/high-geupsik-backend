package com.highgeupsik.backend.service;


import com.highgeupsik.backend.dto.UploadFileDTO;
import com.highgeupsik.backend.entity.UploadFile;
import com.highgeupsik.backend.repository.UploadFileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UploadFileService {

    private final UploadFileRepository uploadFileRepository;

    public void saveUploadFile(List<UploadFileDTO> imageList){
        for (UploadFileDTO uploadFileDTO : imageList) {
            uploadFileRepository.save(new UploadFile(uploadFileDTO.getFileName(),
                uploadFileDTO.getFileDownloadUri()));
        }
    }

}
