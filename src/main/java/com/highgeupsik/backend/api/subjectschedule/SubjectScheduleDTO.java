package com.highgeupsik.backend.api.subjectschedule;

import java.util.List;
import lombok.Getter;

@Getter
public class SubjectScheduleDTO {

    List<SubjectDTO> monDay;
    List<SubjectDTO> tuesDay;
    List<SubjectDTO> wednesDay;
    List<SubjectDTO> thursDay;
    List<SubjectDTO> friDay;

    public SubjectScheduleDTO(List<SubjectDTO> monDay, List<SubjectDTO> tuesDay,
        List<SubjectDTO> wednesDay, List<SubjectDTO> thursDay, List<SubjectDTO> friDay) {
        this.monDay = monDay;
        this.tuesDay = tuesDay;
        this.wednesDay = wednesDay;
        this.thursDay = thursDay;
        this.friDay = friDay;
    }
}
