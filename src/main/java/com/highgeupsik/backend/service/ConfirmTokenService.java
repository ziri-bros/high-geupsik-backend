package com.highgeupsik.backend.service;


import com.highgeupsik.backend.entity.ConfirmToken;
import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.repository.ConfirmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class ConfirmTokenService {

    private static final long TIME_LIMIT = 5L;

    private final ConfirmTokenRepository confirmTokenRepository;

    public String saveConfirmToken(Long userId){
        return confirmTokenRepository.save(ConfirmToken.builder()
                .userId(userId)
                .expirationDate(LocalDateTime.now().plusMinutes(TIME_LIMIT)).build())
                .getId();
    }

    /**
     * 유효한 토큰 가져오기
     */
    public ConfirmToken findOneByIdNotExpired(String emailTokenId){
        return confirmTokenRepository.findByIdAndExpirationDateAfterAndExpired(emailTokenId,
                LocalDateTime.now(),false).orElseThrow(() -> new NotFoundException(TOKEN_EXPIRED));
    }

    public String findByUserId(Long userId) {

        ConfirmToken confirmToken = confirmTokenRepository.findByUserId(userId).get();
        return confirmToken.getId();

    }

    public ConfirmToken findByUserIdAndExpired(Long userId){
        return confirmTokenRepository.findByUserIdAndExpired(userId, true)
                .orElseThrow(() -> new NotFoundException("토큰이 없습니다"));
    }

    public void deletePrevToken(String tokenId) {
        confirmTokenRepository.deleteById(tokenId);
    }
}
