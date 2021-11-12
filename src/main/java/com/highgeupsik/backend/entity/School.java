package com.highgeupsik.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SCHOOL")
@Entity
public class School {

    @Id
    @Column(name = "SCHOOL_ID", updatable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, updatable = false)
    private String name;

    @Column(name = "CODE", nullable = false, updatable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    private Region region;

    public School(String name, String code, Region region) {
        this.name = name;
        this.code = code;
        this.region = region;
    }

}
