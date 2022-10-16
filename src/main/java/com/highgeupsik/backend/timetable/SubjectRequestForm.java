package com.highgeupsik.backend.timetable;

import lombok.Getter;

@Getter
public class SubjectRequestForm {

    String regionCode;
    String schoolCode;
    int grade;
    int classNm;

    public SubjectRequestForm(String regionCode, String schoolCode, int grade, int classNm) {
        this.regionCode = regionCode;
        this.schoolCode = schoolCode;
        this.grade = grade;
        this.classNm = classNm;
    }
}
