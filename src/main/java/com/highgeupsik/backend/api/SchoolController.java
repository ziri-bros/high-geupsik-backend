package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;

import com.highgeupsik.backend.dto.SchoolResDTO;
import com.highgeupsik.backend.dto.SchoolSearchCondition;
import com.highgeupsik.backend.service.SchoolQueryService;
import com.highgeupsik.backend.utils.ApiResult;

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
