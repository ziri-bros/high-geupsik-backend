package com.highgeupsik.backend.service;


import static com.highgeupsik.backend.utils.ErrorMessage.SCHOOL_NOT_FOUND;
import static com.highgeupsik.backend.utils.ErrorMessage.USER_NOT_FOUND;

import com.highgeupsik.backend.dto.SchoolDTO;

import com.highgeupsik.backend.entity.UserCard;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.SchoolRepository;
import com.highgeupsik.backend.repository.UserCardRepository;
import com.highgeupsik.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCardService {

    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;

    public Long saveUserCard(Long userId, String thumbnail, SchoolDTO schoolDTO) {
        return userCardRepository.save(UserCard.builder()
            .user(userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(USER_NOT_FOUND)))
            .thumbnail(thumbnail)
            .school(schoolRepository.findByName(schoolDTO.getName()).orElseThrow(() ->
                new NotFoundException(SCHOOL_NOT_FOUND)))
            .build()).getId();
    }

    public void deleteUserCard(Long cardId) {
        userCardRepository.deleteById(cardId);
    }

    public void deleteUserCardByUserId(Long userId) {
        userCardRepository.deleteByUserId(userId);
    }
}
