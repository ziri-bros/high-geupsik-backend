package com.highgeupsik.backend.service;

import com.highgeupsik.backend.dto.SchoolResDTO;
import com.highgeupsik.backend.dto.SchoolSearchCondition;
import com.highgeupsik.backend.repository.SchoolRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SchoolQueryService {

    private final SchoolRepository schoolRepository;

    public List<SchoolResDTO> findAllByRegionAndName(SchoolSearchCondition condition) {
        return schoolRepository
            .findAllByRegionAndName(condition)
            .stream()
            .map(SchoolResDTO::new)
            .collect(Collectors.toList());
    }
}
