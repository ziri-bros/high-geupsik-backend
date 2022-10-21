package com.highgeupsik.backend.entity.school;

import com.highgeupsik.backend.api.school.neis.SchoolInfoRes;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@TableGenerator(
    name = "SCHOOL_SEQ_GENERATOR",
    table = "MY_SEQUENCES",
    pkColumnValue = "SCHOOL_SEQ", allocationSize = 500
)
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SCHOOL_SEQ_GENERATOR")
    @Column(name = "school_id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "school_code", nullable = false)
    private String schoolCode;

    @Column(name = "region_code", nullable = false)
    private String regionCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    private Region region;

    @Column(name = "homepage_url")
    private String homepageUrl;

    public School(SchoolInfoRes schoolInfoRes) {
        name = schoolInfoRes.getSCHUL_NM();
        schoolCode = schoolInfoRes.getSD_SCHUL_CODE();
        regionCode = schoolInfoRes.getATPT_OFCDC_SC_CODE();
        region = Region.valueOf(schoolInfoRes.getLCTN_SC_NM());
        homepageUrl = schoolInfoRes.getHMPG_ADRES();
    }
}
