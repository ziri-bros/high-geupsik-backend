package com.highgeupsik.backend.service;

import com.highgeupsik.backend.dto.UserResDTO;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.UserRepository;
import com.highgeupsik.backend.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.highgeupsik.backend.utils.ErrorMessage.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserResDTO findById(Long userId) {
        return new UserResDTO(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
