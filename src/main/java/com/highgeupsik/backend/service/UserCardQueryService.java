package com.highgeupsik.backend.service;


import com.highgeupsik.backend.dto.UserCardResDTO;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.UserCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.highgeupsik.backend.utils.ErrorMessage.*;
import static com.highgeupsik.backend.utils.PagingUtils.*;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCardQueryService {

    private static final int CARD_COUNT = 20;
    private final UserCardRepository userCardRepository;

    public Page<UserCardResDTO> findUserCards(Integer pageNum) {
        return userCardRepository.findUserCards(orderByCreatedDateASC(pageNum, CARD_COUNT));
    }

    public Long findUserIdByCardId(Long cardId){
        return userCardRepository.findById(cardId).orElseThrow(
                () -> new NotFoundException(CARD_NOT_FOUND)).getUser().getId();
    }

    public boolean findByUserId(Long userId){
        return userCardRepository.findById(userId).isPresent();
    }

}
