package com.highgeupsik.backend.timetable;

import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;
import static com.highgeupsik.backend.utils.UrlUtils.*;

import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.UserRepository;
import com.highgeupsik.backend.timetable.neis.OpenApiService;
import com.highgeupsik.backend.timetable.neis.Schedule;
import com.highgeupsik.backend.utils.MyDateUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import service.CacheStoreService;

@RequiredArgsConstructor
@Service
public class TimetableService {

    private final UserRepository userRepository;
    private final OpenApiService apiService;
    private final CacheStoreService<String, TimetableDTO> cacheStoreService;

    public TimetableDTO findTimetable(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        SubjectRequestForm requestForm = new SubjectRequestForm(
            user.getSchool().getRegionCode(),
            user.getSchool().getCode(),
            user.getStudentCard().getGrade().getGradeNum(), user.getStudentCard().getClassNum());
        String cacheKey = getCacheKey(requestForm);
        TimetableDTO timetableDTO = cacheStoreService.findByKey(cacheKey);
        if (Objects.isNull(timetableDTO)) {
            String[] dates = MyDateUtils.getWeekDates();
            timetableDTO = new TimetableDTO(
                getSchedules(getScheduleRequestUrl(requestForm, dates[1])),
                getSchedules(getScheduleRequestUrl(requestForm, dates[2])),
                getSchedules(getScheduleRequestUrl(requestForm, dates[3])),
                getSchedules(getScheduleRequestUrl(requestForm, dates[4])),
                getSchedules(getScheduleRequestUrl(requestForm, dates[5]))
            );
        }
        cacheStoreService.save(cacheKey, timetableDTO);
        return timetableDTO;
    }

    private List<Subject> getSchedules(String url) {
        return apiService.getRequest(url, Schedule.class).getTimeTable()
            .stream()
            .map((t) -> new Subject(t.getPERIO(), t.getITRT_CNTNT(), t.getCLRM_NM()))
            .collect(Collectors.toList());
    }

    private String getCacheKey(SubjectRequestForm subjectRequestForm) {
        return subjectRequestForm.getRegionCode()
            + subjectRequestForm.schoolCode
            + subjectRequestForm.getGrade()
            + subjectRequestForm.getClassNm();
    }
}
