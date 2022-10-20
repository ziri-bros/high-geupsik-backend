package com.highgeupsik.backend.api.image;

import com.highgeupsik.backend.api.ApiResult;
import com.highgeupsik.backend.api.ApiUtils;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.highgeupsik.backend.service.aws.S3Service;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ImageApiController {

    private final S3Service s3Service;

    @ApiOperation(value = "이미지 업로드")
    @PostMapping("/images")
    public ApiResult<List<UploadFileDTO>> uploadImage(List<MultipartFile> imageList) {
        return ApiUtils.success(s3Service.uploadFiles(imageList));
    }
}
