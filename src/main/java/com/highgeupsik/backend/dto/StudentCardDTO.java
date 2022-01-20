package com.highgeupsik.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class StudentCardDTO {

    private int grade;
    private int classNum;
    private String studentCardImage;
}
