package com.highgeupsik.backend.dto;


import com.highgeupsik.backend.entity.Region;
import com.highgeupsik.backend.entity.School;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolDTO {

    private String name;
    private String code;
    private Region region;

    public SchoolDTO(School school){
        name = school.getName();
        code = school.getCode();
        region = school.getRegion();
    }

}
