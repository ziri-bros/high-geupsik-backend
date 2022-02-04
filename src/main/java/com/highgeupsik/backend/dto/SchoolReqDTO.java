package com.highgeupsik.backend.dto;

import com.highgeupsik.backend.entity.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SchoolReqDTO {

    Region region;
    String schoolName;
}
