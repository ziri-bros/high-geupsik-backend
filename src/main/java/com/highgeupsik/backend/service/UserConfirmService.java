package com.highgeupsik.backend.service;

import com.highgeupsik.backend.dto.UserConfirmDTO;
import com.highgeupsik.backend.repository.UserConfirmRepository;
import com.highgeupsik.backend.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserConfirmService {

    private final UserConfirmRepository userConfirmRepository;
    private static final int USER_CONFIRM_COUNT = 20;

    public Page<UserConfirmDTO> findAll(Integer pageNum) {
        return userConfirmRepository.findAll(PagingUtils.orderByCreatedDateASC(pageNum, USER_CONFIRM_COUNT))
            .map(UserConfirmDTO::new);
    }
}
