package com.highgeupsik.backend.service;

import com.highgeupsik.backend.dto.SubjectScheduleDTO;
import com.highgeupsik.backend.entity.Subject;
import com.highgeupsik.backend.entity.SubjectSchedule;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.SubjectRepository;
import com.highgeupsik.backend.repository.SubjectScheduleRepository;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectScheduleService {

    private final SubjectScheduleRepository subjectScheduleRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public Long saveSubjectSchedule(SubjectScheduleDTO subjectScheduleDTO, Long userId) {
        SubjectSchedule subjectSchedule = subjectScheduleRepository.save(SubjectSchedule.builder()
                .subjectList(subjectScheduleDTO.getSubjectDTOList()
                        .stream().map((subjectDTO -> new Subject(subjectDTO.getSubjectTime(),
                                subjectDTO.getWeekDay(), subjectDTO.getSubjectName())))
                        .collect(Collectors.toList()))
                .build());
        subjectSchedule.getSubjectList().forEach((subject -> subject.setSubjectSchedule(subjectSchedule)));
        userRepository.findById(userId).get().setSubjectSchedule(subjectSchedule);
        return subjectSchedule.getId();
    }

    public void changeSubjectSchedule(SubjectScheduleDTO subjectScheduleDTO, Long userId) {
        SubjectSchedule subjectSchedule = subjectScheduleRepository.findOneByUserId(userId).orElseThrow(()
                -> new NotFoundException("시간표가 없습니다"));
        subjectRepository.deleteBySubjectScheduleId(subjectSchedule.getId());
        subjectSchedule.changeSubjects(subjectScheduleDTO);
    }

    public void deleteSchedule(Long userId) {
        SubjectSchedule subjectSchedule = subjectScheduleRepository.findOneByUserId(userId).get();
        subjectScheduleRepository.delete(subjectSchedule);
    }

    public Long makeSubjectSchedule(SubjectScheduleDTO subjectScheduleDTO, Long userId) {
        Optional<SubjectSchedule> subject = subjectScheduleRepository.findOneByUserId(userId);
        if(subject.isPresent()){
            changeSubjectSchedule(subjectScheduleDTO,userId);
            return subject.get().getId();
        }
        return saveSubjectSchedule(subjectScheduleDTO,userId);
    }
}
