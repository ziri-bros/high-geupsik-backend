package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.SubjectSchedule;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SubjectScheduleDTO {

    private List<SubjectDTO> subjectDTOList = new ArrayList<>();

    public SubjectScheduleDTO(SubjectSchedule subjectSchedule) {
        subjectDTOList = subjectSchedule.getSubjectList().stream().map(SubjectDTO::new)
            .collect(Collectors.toList());
    }
}
