package com.highgeupsik.backend.utils;

import com.highgeupsik.backend.entity.Region;
import com.highgeupsik.backend.entity.School;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class ParsingUtils {

    public static List<School> getSchoolListFromJsonFile(ClassPathResource resource) {
        String REGION = "LCTN_SC_NM"; //소재지명
        String SCHOOLCODE = "SD_SCHUL_CODE"; //표준학교코드
        String SCHOOLNAME = "SCHUL_NM"; //학교명
        String REGIONCODE = "ATPT_OFCDC_SC_CODE"; //시도교육청코드
        String SCHOOLTYPE = "SCHUL_KND_SC_NM"; //학교종류
        List<School> schoolList = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            InputStreamReader streamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            for (int i = 1; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.get(SCHOOLTYPE).equals("중학교") || jsonObject.get(SCHOOLTYPE).equals("초등학교")) {
                    continue;
                }
                schoolList.add(new School(
                    (String) jsonObject.get(SCHOOLNAME),
                    (String) jsonObject.get(SCHOOLCODE),
                    (String) jsonObject.get(REGIONCODE),
                    Region.valueOf((String) jsonObject.get(REGION))));
            }
        } catch (IOException e) {
            log.info("파일에러입니다");
        } catch (ParseException e) {
            log.info("파싱에러입니다");
        }
        return schoolList;
    }
}
