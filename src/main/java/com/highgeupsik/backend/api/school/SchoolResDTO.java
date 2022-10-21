package com.highgeupsik.backend.api.school;

import com.highgeupsik.backend.entity.school.Region;
import com.highgeupsik.backend.entity.school.School;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class SchoolResDTO {

    private String name;
    private String schoolCode;
    private String regionCode;
    private Region region;
    private String homepageUrl;

    public SchoolResDTO(School school) {
        name = school.getName();
        schoolCode = school.getSchoolCode();
        regionCode = school.getRegionCode();
        region = school.getRegion();
        homepageUrl = school.getHomepageUrl();
    }
}
