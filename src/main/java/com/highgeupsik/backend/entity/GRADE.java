package com.highgeupsik.backend.entity;

import static com.highgeupsik.backend.utils.ErrorMessage.GRADE_NOT_FOUND;

import com.highgeupsik.backend.exception.NotFoundException;
import com.highgeupsik.backend.utils.ErrorMessage;
import java.util.Arrays;

public enum GRADE {
    FIRST(1),
    SECOND(2),
    THIRD(3);

    private final int gradeNum;

    GRADE(int gradeNum) {
        this.gradeNum = gradeNum;
    }

    public static GRADE from(int gradeNum) {
        return Arrays.stream(values())
            .filter(grade -> grade.gradeNum == gradeNum)
            .findAny()
            .orElseThrow(() -> new NotFoundException(GRADE_NOT_FOUND));
    }

    public int getGradeNum() {
        return gradeNum;
    }
}
