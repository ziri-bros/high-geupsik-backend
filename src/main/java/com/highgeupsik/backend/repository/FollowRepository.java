package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    public Optional<Follow> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    public Page<Follow> findByToUserIdAndAcceptFlag(Long toUserId, boolean acceptFlag, Pageable pageable);

    public Page<Follow> findByFromUserIdAndAcceptFlag(Long fromUserId, boolean acceptFlag, Pageable pageable);

}
