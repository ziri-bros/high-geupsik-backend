package com.highgeupsik.backend.service;


import com.highgeupsik.backend.dto.UserConfirmDTO;
import com.highgeupsik.backend.entity.User;
import com.highgeupsik.backend.entity.UserConfirm;
import com.highgeupsik.backend.repository.UserConfirmRepository;
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
    private static final int USER_CONFIRM_COUNT = 20;

    public Long saveUserConfirm(User user) {
        return userConfirmRepository.save(UserConfirm.builder()
            .user(user)
            .studentCard(user.getStudentCard())
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
