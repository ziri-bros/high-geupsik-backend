package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.SubjectSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectScheduleRepository extends JpaRepository<SubjectSchedule, Long> {

    Optional<SubjectSchedule> findOneByUserId(Long userId);
}
