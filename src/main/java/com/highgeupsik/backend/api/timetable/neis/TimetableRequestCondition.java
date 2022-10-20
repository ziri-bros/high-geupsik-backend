package com.highgeupsik.backend.api.timetable.neis;

import com.highgeupsik.backend.entity.School;
import com.highgeupsik.backend.entity.StudentCard;
import lombok.Getter;

@Getter
public class TimetableRequestCondition {

    String regionCode;
    String schoolCode;
    int grade;
    int classNm;

    public TimetableRequestCondition(School school, StudentCard studentCard){
        regionCode = school.getRegionCode();
        schoolCode = school.getCode();
        grade = studentCard.getGrade().getGradeNum();
        classNm = studentCard.getClassNum();
    }
}
