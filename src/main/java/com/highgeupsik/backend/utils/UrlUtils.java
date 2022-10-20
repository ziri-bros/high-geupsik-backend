package com.highgeupsik.backend.utils;

import com.highgeupsik.backend.api.subjectschedule.neis.TimetableRequestCondition;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlUtils {

    public static String getScheduleRequestUrl(TimetableRequestCondition requestForm, String date) {
        return "https://open.neis.go.kr/hub/hisTimetable?Type=json&"
            + "ATPT_OFCDC_SC_CODE=" + requestForm.getRegionCode() + "&"
            + "SD_SCHUL_CODE=" + requestForm.getSchoolCode() + "&"
            + "GRADE=" + requestForm.getGrade() + "&"
            + "CLASS_NM=" + requestForm.getClassNm() + "&"
            + "ALL_TI_YMD=" + date;
    }
}
