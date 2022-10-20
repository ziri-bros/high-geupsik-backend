package com.highgeupsik.backend.service.timetable;

import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;
import static com.highgeupsik.backend.utils.UrlUtils.getScheduleRequestUrl;

import com.highgeupsik.backend.api.timetable.SubjectDto;
import com.highgeupsik.backend.api.timetable.TimetableDto;
import com.highgeupsik.backend.api.timetable.neis.TimetableRequestCondition;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.UserRepository;
import com.highgeupsik.backend.utils.OpenApiRequestUtils;
import com.highgeupsik.backend.api.timetable.neis.Schedule;
import com.highgeupsik.backend.utils.MyDateUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TimetableService {

    private final UserRepository userRepository;

    public TimetableDto findTimetable(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        TimetableRequestCondition requestForm = new TimetableRequestCondition(user.getSchool(), user.getStudentCard());
        String[] dates = MyDateUtils.getWeekDates();
        return new TimetableDto(
                getSchedules(getScheduleRequestUrl(requestForm, dates[1])),
                getSchedules(getScheduleRequestUrl(requestForm, dates[2])),
                getSchedules(getScheduleRequestUrl(requestForm, dates[3])),
                getSchedules(getScheduleRequestUrl(requestForm, dates[4])),
                getSchedules(getScheduleRequestUrl(requestForm, dates[5]))
            );
    }

    private List<SubjectDto> getSchedules(String url) {
        return OpenApiRequestUtils.getRequest(url, Schedule.class).getTimeTable()
            .stream()
            .map((t) -> new SubjectDto(t.getPERIO(), t.getITRT_CNTNT(), t.getCLRM_NM()))
            .collect(Collectors.toList());
    }
}
