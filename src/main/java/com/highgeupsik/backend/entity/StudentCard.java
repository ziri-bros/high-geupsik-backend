package com.highgeupsik.backend.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentCard extends TimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "student_card_id")
    private Long id;

    private String studentCardImage;

    @OneToOne(mappedBy = "studentCard")
    private User user;

    @Builder
    public StudentCard(String studentCardImage) {
        this.studentCardImage = studentCardImage;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
