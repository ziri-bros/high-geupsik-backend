package com.highgeupsik.backend.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"email", "provider"}
        )
    }
)
@Entity
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "student_card_id")
    private StudentCard studentCard;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_schedule_id")
    private SubjectSchedule subjectSchedule;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @OneToMany(mappedBy = "user")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public User(String email, String username, AuthProvider provider,
        Role role) {
        this.email = email;
        this.username = username;
        this.provider = provider;
        this.role = role;
    }

    public void setStudentCard(StudentCard studentCard) {
        this.studentCard = studentCard;
        studentCard.setUser(this);
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void updateRoleUser() {
        role = Role.ROLE_USER;
    }

    public void updateRoleGuest() {
        role = Role.ROLE_GUEST;
    }

    public User updateName(String username) {
        this.username = username;
        return this;
    }

    public void setSubjectSchedule(SubjectSchedule subjectSchedule) {
        this.subjectSchedule = subjectSchedule;
        subjectSchedule.setUser(this);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
