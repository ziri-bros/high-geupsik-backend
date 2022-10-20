package com.highgeupsik.backend.repository.user;

import com.highgeupsik.backend.entity.school.StudentCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCardRepository extends JpaRepository<StudentCard, Long> {

}
