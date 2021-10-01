package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SchoolInfoDTO {

    private String schoolName;

    private String schoolCode;

    private Region region;

}
