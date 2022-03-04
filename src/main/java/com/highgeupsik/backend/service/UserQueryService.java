package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;

import com.highgeupsik.backend.dto.UserResDTO;
import com.highgeupsik.backend.exception.ResourceNotFoundException;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserQueryService {

    private final UserRepository userRepository;

    public UserResDTO findById(Long userId) {
        return new UserResDTO(userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND)));
    }
}
