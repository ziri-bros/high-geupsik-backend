package com.highgeupsik.backend.api;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.highgeupsik.backend.dto.UploadFileDTO;
import com.highgeupsik.backend.service.S3Service;
import com.highgeupsik.backend.utils.ApiResult;
import com.highgeupsik.backend.utils.ApiUtils;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ImageApiController {

    private final S3Service s3Service;

    @ApiOperation(value = "이미지 업로드")
    @PostMapping("/images")
    public ApiResult<List<UploadFileDTO>> imageSave(List<MultipartFile> imageList) {
        return ApiUtils.success(s3Service.uploadFiles(imageList));
    }
}
