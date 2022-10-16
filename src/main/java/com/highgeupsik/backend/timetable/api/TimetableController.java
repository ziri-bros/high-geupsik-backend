package com.highgeupsik.backend.timetable.api;

import static com.highgeupsik.backend.utils.ApiUtils.success;

import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.timetable.TimetableService;
import com.highgeupsik.backend.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TimetableController {

    private final TimetableService timetableService;

    @GetMapping("/timetables")
    public ApiResult timetables(@LoginUser Long userId){
        return success(timetableService.findTimetable(userId));
    }
}
