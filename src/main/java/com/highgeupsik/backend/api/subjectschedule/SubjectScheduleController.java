package com.highgeupsik.backend.api.subjectschedule;


import static com.highgeupsik.backend.api.ApiUtils.success;

import com.highgeupsik.backend.api.ApiResult;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.SubjectScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SubjectScheduleController {

    private final SubjectScheduleService subjectScheduleService;

    @GetMapping("/subject-schedules")
    public ApiResult timetables(@LoginUser Long userId){
        return success(subjectScheduleService.findTimetable(userId));
    }
}
