package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    void deleteBySubjectScheduleId(Long subjectScheduleId);
}
