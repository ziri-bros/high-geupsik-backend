package com.highgeupsik.backend.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentCardDTO {

    private int grade;
    private int classNum;
    private String studentCardImage;

}
