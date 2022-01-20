package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Subject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class SubjectDTO {

    private int weekDay;
    private int subjectTime;
    private String subjectName;

    public SubjectDTO(Subject subject) {
        weekDay = subject.getWeekDay();
        subjectTime = subject.getSubjectTime();
        subjectName = subject.getSubjectName();
    }
}
