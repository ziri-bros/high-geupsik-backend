package com.highgeupsik.backend.entity;


import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class SchoolInfo {

    private String schoolName;

    private String schoolCode;

    @Enumerated(EnumType.STRING)
    private Region region;

    public SchoolInfo(String schoolName, String schoolCode, Region region) {
        this.schoolName = schoolName;
        this.schoolCode = schoolCode;
        this.region = region;
    }

}
