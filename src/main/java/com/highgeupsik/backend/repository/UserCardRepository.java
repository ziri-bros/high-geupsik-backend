package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.UserCard;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserCardRepository extends JpaRepository<UserCard, Long>, UserCardRepositoryCustom {

    void deleteByUserId(Long userId);

    public Optional<UserCard> findByUserId(Long userId);
}
