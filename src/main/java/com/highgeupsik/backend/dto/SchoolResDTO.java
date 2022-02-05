package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Region;
import com.highgeupsik.backend.entity.School;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class SchoolResDTO {

    private String name;
    private String code;
    private String regionCode;
    private Region region;

    public SchoolResDTO(School school) {
        name = school.getName();
        code = school.getCode();
        regionCode = school.getRegionCode();
        region = school.getRegion();
    }
}
