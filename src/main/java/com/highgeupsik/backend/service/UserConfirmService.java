package com.highgeupsik.backend.service;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.dto.UserConfirmDTO;
import com.highgeupsik.backend.entity.UserConfirm;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.StudentCardRepository;
import com.highgeupsik.backend.repository.UserConfirmRepository;
import com.highgeupsik.backend.repository.UserRepository;
import com.highgeupsik.backend.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserConfirmService {

    private final UserConfirmRepository userConfirmRepository;
    private final UserRepository userRepository;
    private final StudentCardRepository studentCardRepository;
    private static final int USER_CONFIRM_COUNT = 20;

    public Long saveUserConfirm(Long userId) {
        return userConfirmRepository.save(UserConfirm.builder()
            .user(userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND)))
            .studentCard(studentCardRepository.findByUserId(userId).orElseThrow(
                () -> new NotFoundException(CARD_NOT_FOUND)))
            .build()).getId();
    }

    public Page<UserConfirmDTO> findAll(Integer pageNum){
        return userConfirmRepository.findAll(PagingUtils.orderByCreatedDateASC(pageNum, USER_CONFIRM_COUNT))
            .map(UserConfirmDTO::new);
    }

    public void deleteUserConfirmByUserId(Long userId){
        userConfirmRepository.deleteByUserId(userId);
    }

}
