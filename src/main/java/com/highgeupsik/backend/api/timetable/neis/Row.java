package com.highgeupsik.backend.api.timetable.neis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Row {

  private final List<Timetable> row = new ArrayList<>();
}