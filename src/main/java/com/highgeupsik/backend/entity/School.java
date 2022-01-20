package com.highgeupsik.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Column(name = "code", nullable = false, updatable = false)
    private String code;

    @Column(name = "region_code", nullable = false, updatable = false)
    private String regionCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false, updatable = false)
    private Region region;

    public School(String name, String code, String regionCode, Region region) {
        this.name = name;
        this.code = code;
        this.regionCode = regionCode;
        this.region = region;
    }
}
