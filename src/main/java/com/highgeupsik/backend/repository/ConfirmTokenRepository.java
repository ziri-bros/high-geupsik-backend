package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.ConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmTokenRepository extends JpaRepository<ConfirmToken, String> {

    Optional<ConfirmToken> findByUserId(Long userId);

    Optional<ConfirmToken> findByUserIdAndExpired(Long userId, boolean expired);

    Optional<ConfirmToken> findByIdAndExpirationDateAfterAndExpired(String confirmTokenId, LocalDateTime now, boolean expired);

}
