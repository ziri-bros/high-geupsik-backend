package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.StudentCard;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentCardRepository extends JpaRepository<StudentCard, Long> {

    Optional<StudentCard> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
