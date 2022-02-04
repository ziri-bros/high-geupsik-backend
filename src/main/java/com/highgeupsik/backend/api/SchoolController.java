package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;

import com.highgeupsik.backend.dto.SchoolReqDTO;
import com.highgeupsik.backend.service.SchoolService;
import com.highgeupsik.backend.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping("/schools")
    public ApiResult schoolDetails(@RequestBody SchoolReqDTO schoolReqDTO) {
        return success(schoolService.findByRegionAndName(schoolReqDTO.getRegion(), schoolReqDTO.getSchoolName()));
    }
}
