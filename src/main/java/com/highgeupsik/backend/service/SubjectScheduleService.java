package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.dto.SubjectScheduleDTO;
import com.highgeupsik.backend.entity.Subject;
import com.highgeupsik.backend.entity.SubjectSchedule;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.SubjectScheduleRepository;
import com.highgeupsik.backend.repository.UserRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SubjectScheduleService {

    private final SubjectScheduleRepository subjectScheduleRepository;
    private final UserRepository userRepository;

    public void saveSubjectSchedule(SubjectScheduleDTO subjectScheduleDTO, Long userId) {
        SubjectSchedule subjectSchedule = subjectScheduleRepository.save(SubjectSchedule.builder()
            .subjectList(subjectScheduleDTO.getSubjectDTOList()
                .stream().map((subjectDTO -> new Subject(subjectDTO.getSubjectTime(),
                    subjectDTO.getWeekDay(), subjectDTO.getSubjectName())))
                .collect(Collectors.toList()))
            .build());
        subjectSchedule.getSubjectList().forEach((subject -> subject.setSubjectSchedule(subjectSchedule)));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND))
            .setSubjectSchedule(subjectSchedule);
    }
}
