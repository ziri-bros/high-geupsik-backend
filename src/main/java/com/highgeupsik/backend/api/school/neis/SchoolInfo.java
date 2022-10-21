package com.highgeupsik.backend.api.school.neis;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class SchoolInfo {

    private final List<Row> schoolInfo = new ArrayList<>();

    public List<SchoolInfoRes> getSchoolInfoRes() {
        return schoolInfo.get(1).getRow();
    }
}
