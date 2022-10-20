package com.highgeupsik.backend.api.subjectschedule.neis;

import com.highgeupsik.backend.entity.school.School;
import com.highgeupsik.backend.entity.school.StudentCard;
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
