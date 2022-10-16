package com.highgeupsik.backend.timetable.neis;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Schedule {

  List<Row> hisTimetable = new ArrayList<>();

  public List<Timetable> getTimeTable() {
    return hisTimetable.get(1).getRow();
  }
}