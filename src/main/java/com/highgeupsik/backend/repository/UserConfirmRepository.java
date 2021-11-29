package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.UserConfirm;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConfirmRepository extends JpaRepository<UserConfirm, Long> {

    @EntityGraph(attributePaths = {"user","studentCard"})
    Page<UserConfirm> findAll(Pageable pageable);

    Optional<UserConfirm> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
