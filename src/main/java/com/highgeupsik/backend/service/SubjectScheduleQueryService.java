package com.highgeupsik.backend.service;


import com.highgeupsik.backend.dto.SubjectScheduleDTO;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.SubjectScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectScheduleQueryService {

    private final SubjectScheduleRepository subjectScheduleRepository;

    public SubjectScheduleDTO findSubjectSchedule(Long userId){
        return new SubjectScheduleDTO(subjectScheduleRepository.findOneByUserId(userId).orElseThrow(
                ()-> new NotFoundException("시간표가 없습니다")));
    }

}
