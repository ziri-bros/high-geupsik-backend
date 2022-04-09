package com.highgeupsik.backend.dto;

import static com.highgeupsik.backend.utils.ErrorMessage.*;

import com.highgeupsik.backend.entity.Category;
import com.highgeupsik.backend.entity.Region;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BoardSearchCondition {

    @NotNull(message = REGION_NOT_NULL)
    private Region region;
    @NotNull(message = CATEGORY_NOT_NULL)
    private Category category;

    private String keyword;

    private Integer likeCount;
}
