package com.highgeupsik.backend.api.timetable.neis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Timetable {

  String ALL_TI_YMD;
  String GRADE; //학년
  String CLASS_NM; //반
  String CLRM_NM; //강의실
  String PERIO; //몇교시
  String ITRT_CNTNT; //과목명
}