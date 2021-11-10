package com.highgeupsik.backend.entity;


import com.highgeupsik.backend.dto.SubjectScheduleDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubjectSchedule {

    @Id
    @GeneratedValue
    @Column(name = "subject_schedule_id")
    private Long id;

    @OneToOne(mappedBy = "subjectSchedule", fetch = FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "subjectSchedule", cascade = CascadeType.ALL)
    private List<Subject> subjectList = new ArrayList<>();

    @Builder
    public SubjectSchedule(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public void changeSubjects(SubjectScheduleDTO subjectScheduleDTO) {
        subjectList = subjectScheduleDTO.getSubjectDTOList()
            .stream().map((subjectDTO -> new Subject(
                subjectDTO.getSubjectTime(), subjectDTO.getWeekDay(), subjectDTO.getSubjectName()
            ))).collect(Collectors.toList());
        subjectList.forEach((subject -> subject.setSubjectSchedule(this)));
    }

    public void setUser(User user) {
        this.user = user;
    }

}
