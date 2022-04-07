package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;

import com.highgeupsik.backend.dto.SchoolResDTO;
import com.highgeupsik.backend.dto.SchoolSearchCondition;
import com.highgeupsik.backend.service.SchoolQueryService;
import com.highgeupsik.backend.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SchoolController {

    private final SchoolQueryService schoolQueryService;

    @GetMapping("/schools")
    public ApiResult<Page<SchoolResDTO>> schools(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
        SchoolSearchCondition condition) {
        return success(schoolQueryService.findAllByRegionAndName(pageNum, condition));
    }
}
