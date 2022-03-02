package com.highgeupsik.backend.service;

import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final UserRepository userRepository;

    public User login(String loginId, String password) {
        return userRepository.findByEmail(loginId)
            .filter(u -> u.getUsername().equals(password))
            .orElse(null);
    }
}
