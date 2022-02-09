package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.dto.SubjectScheduleDTO;
import com.highgeupsik.backend.entity.Subject;
import com.highgeupsik.backend.entity.SubjectSchedule;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.SubjectScheduleRepository;
import com.highgeupsik.backend.repository.UserRepository;
import java.util.List;
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

        SubjectSchedule subjectSchedule = subjectScheduleRepository.findOneByUserId(userId)
            .orElse(SubjectSchedule.of());
        subjectSchedule.removeSubjects();

        List<Subject> subjectList = subjectScheduleDTO.getSubjectDTOList()
            .stream().map((Subject::of)).collect(Collectors.toList());

        subjectSchedule.setSubjects(subjectList);
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND))
            .setSubjectSchedule(subjectSchedule);

        subjectScheduleRepository.save(subjectSchedule);
    }
}
