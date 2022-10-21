package com.highgeupsik.backend.utils;

import com.highgeupsik.backend.api.subjectschedule.neis.TimetableRequestCondition;
import org.springframework.beans.factory.annotation.Value;

public class UrlGenerator {

    @Value("${open_api_key}")
    private String openApiKey;

    public String getScheduleRequestUrl(TimetableRequestCondition requestCondition, String date) {
        return "https://open.neis.go.kr/hub/hisTimetable?Type=json&"
            + "ATPT_OFCDC_SC_CODE=" + requestCondition.getRegionCode() + "&"
            + "SD_SCHUL_CODE=" + requestCondition.getSchoolCode() + "&"
            + "GRADE=" + requestCondition.getGrade() + "&"
            + "CLASS_NM=" + requestCondition.getClassNm() + "&"
            + "ALL_TI_YMD=" + date + "&"
            + "KEY=" + openApiKey;
    }

    public String getSchoolRequestUrl(int offset) {
        return "https://open.neis.go.kr/hub/schoolInfo?SCHUL_KND_SC_NM=%EA%B3%A0%EB%93%B1%ED%95%99%EA%B5%90&"
            + "Type=json&"
            + "pSize=1000&"
            + "pIndex=" + offset + "&"
            + "KEY=" + openApiKey;
    }
}
