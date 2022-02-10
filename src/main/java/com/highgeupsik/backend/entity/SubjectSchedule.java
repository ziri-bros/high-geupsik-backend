package com.highgeupsik.backend.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class SubjectSchedule {

    @Id
    @GeneratedValue
    @Column(name = "subject_schedule_id")
    private Long id;

    @OneToOne(mappedBy = "subjectSchedule", fetch = FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "subjectSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subject> subjectList = new ArrayList<>();

    @Builder
    public SubjectSchedule(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void removeSubjects() {
        this.getSubjectList().clear();
    }

    public void setSubjects(List<Subject> subjectList) {
        this.subjectList = subjectList;
        subjectList.forEach((subject) -> subject.setSubjectSchedule(this));
    }

    public static SubjectSchedule of() {
        return SubjectSchedule.builder()
            .build();
    }
}
