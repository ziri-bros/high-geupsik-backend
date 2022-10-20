package com.highgeupsik.backend.api.school;

import static com.highgeupsik.backend.api.ApiUtils.*;

import com.highgeupsik.backend.api.ApiResult;
import com.highgeupsik.backend.service.SchoolQueryService;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SchoolController {

    private final SchoolQueryService schoolQueryService;

    @GetMapping("/schools")
    public ApiResult<List<SchoolResDTO>> schools(@Valid SchoolSearchCondition condition) {
        return success(schoolQueryService.findAllByRegionAndName(condition));
    }
}
