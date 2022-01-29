package com.highgeupsik.backend.api;

import static com.highgeupsik.backend.utils.ApiUtils.*;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.highgeupsik.backend.dto.SubjectScheduleDTO;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.SubjectScheduleQueryService;
import com.highgeupsik.backend.service.SubjectScheduleService;
import com.highgeupsik.backend.utils.ApiResult;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController {

    private final SubjectScheduleService subjectScheduleService;
    private final SubjectScheduleQueryService subjectScheduleQueryService;

    @ApiOperation(value = "시간표 조회")
    @GetMapping()
    public ApiResult<SubjectScheduleDTO> scheduleDetails(@LoginUser Long userId) {
        return success(subjectScheduleQueryService.findSubjectSchedule(userId));
    }

    @ApiOperation(value = "시간표 저장")
    @ResponseStatus(OK)
    @PostMapping()
    public void scheduleSave(@LoginUser Long userId,
        @RequestBody SubjectScheduleDTO subjectScheduleDTO) {
        subjectScheduleService.saveSubjectSchedule(subjectScheduleDTO, userId);
    }
}
