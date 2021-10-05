package com.highgeupsik.backend.service;


import com.highgeupsik.backend.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UploadFileService {

    private final UploadFileRepository uploadFileRepository;

}
