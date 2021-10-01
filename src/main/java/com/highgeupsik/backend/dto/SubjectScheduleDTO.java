package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.SubjectSchedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class SubjectScheduleDTO {

    private List<SubjectDTO> subjectDTOList = new ArrayList<>();

    public SubjectScheduleDTO(SubjectSchedule subjectSchedule) {
        subjectDTOList = subjectSchedule.getSubjectList().stream().map((subject) -> new SubjectDTO(subject))
                .collect(Collectors.toList());
    }

}
