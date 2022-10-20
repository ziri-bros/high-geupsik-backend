package com.highgeupsik.backend.utils;

import com.highgeupsik.backend.entity.school.Region;
import com.highgeupsik.backend.entity.school.School;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class ParsingUtils {

    private static final String REGION = "LCTN_SC_NM"; //소재지명
    private static final String SCHOOL_CODE = "SD_SCHUL_CODE"; //표준학교코드
    private static final String SCHOOL_NAME = "SCHUL_NM"; //학교명
    private static final String REGION_CODE = "ATPT_OFCDC_SC_CODE"; //시도교육청코드
    private static final String SCHOOL_TYPE = "SCHUL_KND_SC_NM"; //학교종류
    private static final String HOMEPAGE_URL = "HMPG_ADRES"; //홈페이지 주소

    public static List<School> addSchoolListFromJsonFile(ClassPathResource resource, List<School> schoolList) {
        try {
            JSONParser parser = new JSONParser();
            InputStreamReader streamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                if (jsonObject.get(SCHOOL_TYPE).equals("고등학교")) {
                    schoolList.add(new School(
                        (String) jsonObject.get(SCHOOL_NAME),
                        (String) jsonObject.get(SCHOOL_CODE),
                        (String) jsonObject.get(REGION_CODE),
                        Region.valueOf((String) jsonObject.get(REGION)),
                        (String) jsonObject.get(HOMEPAGE_URL)));
                }
            }
        } catch (IOException e) {
            log.error("파일에러입니다");
        } catch (ParseException e) {
            log.error("파싱에러입니다");
        }
        return schoolList;
    }
}
