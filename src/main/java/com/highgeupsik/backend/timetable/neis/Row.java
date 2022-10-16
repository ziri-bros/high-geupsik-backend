package com.highgeupsik.backend.timetable.neis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Row {

  private List<Timetable> row = new ArrayList<>();
}