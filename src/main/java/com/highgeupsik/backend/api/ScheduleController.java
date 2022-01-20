package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.SubjectScheduleDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.SubjectScheduleQueryService;
import com.highgeupsik.backend.service.SubjectScheduleService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final SubjectScheduleService subjectScheduleService;
    private final SubjectScheduleQueryService subjectScheduleQueryService;

    @ApiOperation(value = "시간표 조회")
    @GetMapping("/users/schedule")
    public ApiResult<SubjectScheduleDTO> schedule(@LoginUser Long userId) {
        return success(subjectScheduleQueryService.findSubjectSchedule(userId));
    }

    @ApiOperation(value = "시간표 제출")
    @PostMapping("/users/schedule")
    public ApiResult makeSchedule(@LoginUser Long userId,
        @RequestBody SubjectScheduleDTO subjectScheduleDTO) {
        return success(subjectScheduleService.makeSubjectSchedule(subjectScheduleDTO, userId));
    }

    @ApiOperation(value = "시간표 삭제")
    @DeleteMapping("/users/schedule")
    public ApiResult deleteSchedule(@LoginUser Long userId) {
        subjectScheduleService.deleteSchedule(userId);
        return success(null);
    }
}
