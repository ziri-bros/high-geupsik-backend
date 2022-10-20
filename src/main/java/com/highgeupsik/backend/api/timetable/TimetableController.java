package com.highgeupsik.backend.api.timetable;


import static com.highgeupsik.backend.api.ApiUtils.success;

import com.highgeupsik.backend.api.ApiResult;
import com.highgeupsik.backend.resolver.LoginUser;
import com.highgeupsik.backend.service.timetable.TimetableService;
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
