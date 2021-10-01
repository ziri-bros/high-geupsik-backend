package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

    public void deleteByBoardDetailId(Long postId);

}
