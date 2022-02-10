package com.highgeupsik.backend.entity;

import com.highgeupsik.backend.dto.SubjectDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Subject {

    @Column(name = "subject_id")
    @Id
    @GeneratedValue
    private Long id;

    private int subjectTime;

    private int weekDay;

    private String subjectName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_schedule_id")
    private SubjectSchedule subjectSchedule;

    @Builder
    public Subject(int subjectTime, int weekDay, String subjectName) {
        this.subjectTime = subjectTime;
        this.subjectName = subjectName;
        this.weekDay = weekDay;
    }

    public void setSubjectSchedule(SubjectSchedule subjectSchedule) {
        this.subjectSchedule = subjectSchedule;
    }

    public static Subject of(SubjectDTO subjectDTO) {
        return Subject.builder()
            .subjectName(subjectDTO.getSubjectName())
            .weekDay(subjectDTO.getWeekDay())
            .subjectName(subjectDTO.getSubjectName())
            .build();
    }
}
