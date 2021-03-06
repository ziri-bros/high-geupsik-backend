package com.highgeupsik.backend.service;

import com.highgeupsik.backend.dto.SubjectScheduleDTO;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.SubjectScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SubjectScheduleQueryService {

    private final SubjectScheduleRepository subjectScheduleRepository;

    public SubjectScheduleDTO findSubjectSchedule(Long userId) {
        return new SubjectScheduleDTO(subjectScheduleRepository.findOneByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("시간표가 없습니다")));
    }
}
