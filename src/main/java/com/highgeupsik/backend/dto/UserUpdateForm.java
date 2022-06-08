package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserUpdateForm {

    private int grade;
    private int classNum;
    private String studentCardImage;
    Region region;
    String schoolName;
}
