package com.highgeupsik.backend.api.school;

import static com.highgeupsik.backend.exception.ErrorMessages.*;

import com.highgeupsik.backend.entity.school.Region;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SchoolSearchCondition {

    @NotNull(message = REGION_NOT_NULL)
    private Region region;
    private String keyword;
}
