package com.highgeupsik.backend.timetable;

import java.util.List;
import lombok.Getter;

@Getter
public class TimetableDTO {

    List<Subject> monDay;
    List<Subject> tuesDay;
    List<Subject> wednesDay;
    List<Subject> thursDay;
    List<Subject> friDay;

    public TimetableDTO(List<Subject> monDay, List<Subject> tuesDay,
        List<Subject> wednesDay, List<Subject> thursDay, List<Subject> friDay) {
        this.monDay = monDay;
        this.tuesDay = tuesDay;
        this.wednesDay = wednesDay;
        this.thursDay = thursDay;
        this.friDay = friDay;
    }
}
