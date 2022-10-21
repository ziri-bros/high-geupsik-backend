package com.highgeupsik.backend.api.subjectschedule.neis;

import com.highgeupsik.backend.entity.school.StudentCard;
import lombok.Getter;

@Getter
public class TimetableRequestCondition {

    String regionCode;
    String schoolCode;
    int grade;
    int classNm;

    public TimetableRequestCondition(StudentCard studentCard){
        regionCode = studentCard.getSchool().getRegionCode();
        schoolCode = studentCard.getSchool().getSchoolCode();
        grade = studentCard.getGrade().getGradeNum();
        classNm = studentCard.getClassNum();
    }
}
