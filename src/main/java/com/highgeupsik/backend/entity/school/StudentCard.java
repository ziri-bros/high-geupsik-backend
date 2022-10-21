package com.highgeupsik.backend.entity.school;

import com.highgeupsik.backend.entity.TimeEntity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class StudentCard extends TimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "student_card_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private GRADE grade;

    private int classNum;

    private String studentCardImage;

    @JoinColumn(name = "school_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private School school;

    public StudentCard(School school, GRADE grade, int classNum, String studentCardImage) {
        this.school = school;
        this.grade = grade;
        this.classNum = classNum;
        this.studentCardImage = studentCardImage;
    }
}
