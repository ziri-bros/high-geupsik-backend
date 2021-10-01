package com.highgeupsik.backend.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"email", "provider"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Embedded
    private SchoolInfo schoolInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_schedule_id")
    private SubjectSchedule subjectSchedule;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @OneToMany(mappedBy = "user")
    private List<BoardDetail> boardDetailList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();


    @Builder
    public User(String email, String username, AuthProvider provider,
                Role role, SchoolInfo schoolInfo) {
        this.email = email;
        this.username = username;
        this.provider = provider;
        this.role = role;
        this.schoolInfo = schoolInfo;
    }

    public void updateRole() {
        role = Role.ROLE_USER;
    }

    public User updateName(String username) {
        this.username = username;
        return this;
    }

    public void updateSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public void setSubjectSchedule(SubjectSchedule subjectSchedule) {
        this.subjectSchedule = subjectSchedule;
        subjectSchedule.setUser(this);
    }

    public void updateToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
