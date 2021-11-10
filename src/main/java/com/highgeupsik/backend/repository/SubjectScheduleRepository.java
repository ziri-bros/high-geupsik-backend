package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.SubjectSchedule;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectScheduleRepository extends JpaRepository<SubjectSchedule, Long> {

    Optional<SubjectSchedule> findOneByUserId(Long userId);
}
