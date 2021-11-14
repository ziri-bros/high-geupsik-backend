package com.highgeupsik.backend.api;

import com.highgeupsik.backend.dto.UploadFileDTO;
import com.highgeupsik.backend.service.S3Service;
import com.highgeupsik.backend.utils.ApiResult;
import com.highgeupsik.backend.utils.ApiUtils;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageApiController {

    private final S3Service s3Service;

    @PostMapping("/images") //이미지 업로드
    public ApiResult<List<UploadFileDTO>> images(List<MultipartFile> imageList) throws IOException {
        return ApiUtils.success(s3Service.uploadFiles(imageList));
    }

}
