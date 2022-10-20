package com.highgeupsik.backend.api.timetable;

import java.util.List;
import lombok.Getter;

@Getter
public class TimetableDto {

    List<SubjectDto> monDay;
    List<SubjectDto> tuesDay;
    List<SubjectDto> wednesDay;
    List<SubjectDto> thursDay;
    List<SubjectDto> friDay;

    public TimetableDto(List<SubjectDto> monDay, List<SubjectDto> tuesDay,
        List<SubjectDto> wednesDay, List<SubjectDto> thursDay, List<SubjectDto> friDay) {
        this.monDay = monDay;
        this.tuesDay = tuesDay;
        this.wednesDay = wednesDay;
        this.thursDay = thursDay;
        this.friDay = friDay;
    }
}
