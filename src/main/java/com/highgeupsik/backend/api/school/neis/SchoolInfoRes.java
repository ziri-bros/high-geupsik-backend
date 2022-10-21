package com.highgeupsik.backend.api.school.neis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolInfoRes {

    private String ATPT_OFCDC_SC_CODE; //지역코드
    private String SD_SCHUL_CODE; //학교코드
    private String SCHUL_NM; //학교이름
    private String LCTN_SC_NM; //서울특별시
    private String HMPG_ADRES; //학교홈페이지
}
