package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.dto.SchoolDTO;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchoolQueryService {

    private final SchoolRepository schoolRepository;

    public SchoolDTO findByName(String name) {
        return new SchoolDTO(schoolRepository.findByName(name).orElseThrow(
            () -> new NotFoundException(SCHOOL_NOT_FOUND)));
    }

}
