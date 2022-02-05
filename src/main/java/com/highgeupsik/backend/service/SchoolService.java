package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.dto.SchoolResDTO;
import com.highgeupsik.backend.entity.Region;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolResDTO findByRegionAndName(Region region, String schoolName) {
        return new SchoolResDTO(schoolRepository.findByRegionAndName(region, schoolName)
            .orElseThrow(() -> new NotFoundException(SCHOOL_NOT_FOUND)));
    }
}
