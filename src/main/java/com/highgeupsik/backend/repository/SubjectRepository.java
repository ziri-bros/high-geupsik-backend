package com.highgeupsik.backend.repository;

import com.highgeupsik.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubjectRepository extends JpaRepository<Subject, Long> {

  //  @Modifying
  //  @Query("delete from Subject s where s.subjectSchedule.id = :subjectScheduleId")
    public void deleteBySubjectScheduleId(Long subjectScheduleId);
}
