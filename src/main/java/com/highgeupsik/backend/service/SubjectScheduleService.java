package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.exception.ErrorMessages.USER_NOT_FOUND;

import com.baechu.cache.datastructure.MyCacheStore;
import com.highgeupsik.backend.api.subjectschedule.SubjectDTO;
import com.highgeupsik.backend.api.subjectschedule.SubjectScheduleDTO;
import com.highgeupsik.backend.api.subjectschedule.neis.Schedule;
import com.highgeupsik.backend.api.subjectschedule.neis.TimetableRequestCondition;
import com.highgeupsik.backend.entity.user.User;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.user.UserRepository;
import com.highgeupsik.backend.utils.MyDateUtils;
import com.highgeupsik.backend.utils.OpenApiRequestUtils;
import com.highgeupsik.backend.utils.UrlGenerator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubjectScheduleService {

    private final UserRepository userRepository;
    private final UrlGenerator urlGenerator;
    private final MyCacheStore<String, SubjectScheduleDTO> cacheStore;

    public SubjectScheduleDTO findScheduleByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        TimetableRequestCondition condition = new TimetableRequestCondition(user.getStudentCard());
        return findScheduleByCustomCache(condition);
    }

    private SubjectScheduleDTO findScheduleByCustomCache(TimetableRequestCondition condition) {
        SubjectScheduleDTO scheduleDTO = cacheStore.get(condition.getKey());
        if (Objects.isNull(scheduleDTO)) {
            String[] dates = MyDateUtils.getWeekDates();
            SubjectScheduleDTO subjectScheduleDTO = new SubjectScheduleDTO(
                getSchedules(urlGenerator.getScheduleRequestUrl(condition, dates[1])),
                getSchedules(urlGenerator.getScheduleRequestUrl(condition, dates[2])),
                getSchedules(urlGenerator.getScheduleRequestUrl(condition, dates[3])),
                getSchedules(urlGenerator.getScheduleRequestUrl(condition, dates[4])),
                getSchedules(urlGenerator.getScheduleRequestUrl(condition, dates[5]))
            );
            cacheStore.put(condition.getKey(), subjectScheduleDTO);
            return subjectScheduleDTO;
        }
        return scheduleDTO;
    }

    @Cacheable(value = "timetable")
    public SubjectScheduleDTO findScheduleBySpringCache(TimetableRequestCondition condition) {
        String[] dates = MyDateUtils.getWeekDates();
        return new SubjectScheduleDTO(
          getSchedules(urlGenerator.getScheduleRequestUrl(condition, dates[1])),
          getSchedules(urlGenerator.getScheduleRequestUrl(condition, dates[2])),
          getSchedules(urlGenerator.getScheduleRequestUrl(condition, dates[3])),
          getSchedules(urlGenerator.getScheduleRequestUrl(condition, dates[4])),
          getSchedules(urlGenerator.getScheduleRequestUrl(condition, dates[5]))
        );
    }

    private List<SubjectDTO> getSchedules(String url) {
        return OpenApiRequestUtils.getRequest(url, Schedule.class).getTimeTable()
            .stream()
            .map((t) -> new SubjectDTO(t.getPERIO(), t.getITRT_CNTNT(), t.getCLRM_NM()))
            .collect(Collectors.toList());
    }
}
